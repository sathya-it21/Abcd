Great — let’s integrate everything together using **Option 2** (pre-signed uploads to S3), and store images as a **comma-separated string** in MySQL.

---

## ✅ 1. Spring Boot Backend

### 📦 `application.properties`

```properties
aws.accessKey=${AWS_ACCESS_KEY}
aws.secretKey=${AWS_SECRET_KEY}
aws.bucketName=barter-courses-bucket
aws.region=ap-south-1
spring.datasource.url=jdbc:mysql://…/yourdb
spring.datasource.username=…
spring.datasource.password=…
```

### 🔧 Add AWS SDK Dependency (pom.xml)

```xml
<dependency>
  <groupId>software.amazon.awssdk</groupId>
  <artifactId>s3</artifactId>
</dependency>
```

### 🧩 Presigned URL Service

```java
@Service
public class PresignedUrlService {
  @Value("${aws.accessKey}") private String accessKey;
  @Value("${aws.secretKey}") private String secretKey;
  @Value("${aws.bucketName}") private String bucketName;
  @Value("${aws.region}") private String region;

  public String generatePresignedUrl(String fileName, String contentType) {
    AwsBasicCredentials creds = AwsBasicCredentials.create(accessKey, secretKey);
    S3Presigner presigner = S3Presigner.builder()
      .region(Region.of(region))
      .credentialsProvider(StaticCredentialsProvider.create(creds))
      .build();

    PutObjectRequest por = PutObjectRequest.builder()
      .bucket(bucketName)
      .key(fileName)
      .contentType(contentType)
      .build();

    PresignedPutObjectRequest preq = presigner.presignPutObject(b -> 
      b.signatureDuration(Duration.ofMinutes(10))
       .putObjectRequest(por)
    );
    presigner.close();
    return preq.url().toString();
  }
}
```

### 🌐 S3 Controller

```java
@RestController
@RequestMapping("/api/s3")
public class S3Controller {
  private final PresignedUrlService presigner;
  @Value("${aws.bucketName}") private String bucketName;
  @Value("${aws.region}") private String region;

  public S3Controller(PresignedUrlService presigner) {
    this.presigner = presigner;
  }

  @GetMapping("/generate-presigned")
  public ResponseEntity<?> getPresigned(
    @RequestParam String fileName,
    @RequestParam String contentType) {
    String url = presigner.generatePresignedUrl(fileName, contentType);
    String publicUrl = String.format("https://%s.s3.%s.amazonaws.com/%s",
        bucketName, region, fileName);
    return ResponseEntity.ok(Map.of("presignedUrl", url, "publicUrl", publicUrl));
  }
}
```

---

### 🗄️ Course Entity & Repository

```java
@Entity
public class Course {
  @Id @GeneratedValue(strategy=IDENTITY)
  private Long id;
  private String title;
  private String description;
  private String level;
  private String features; // comma-separated
  private String courseOutLine;
  private String imageUrls; // comma-separated URLs
  private String videoUrl;
  private Long creatorId;
  private Long categoryId;
  // getters + setters
}

public interface CourseRepository extends JpaRepository<Course,Long> { }
```

### 🛠️ Course Controller

```java
@RestController
@RequestMapping("/api/courses")
public class CourseController {
  private final CourseRepository repo;
  public CourseController(CourseRepository repo){ this.repo = repo; }

  @PostMapping("/insertCourse")
  public ResponseEntity<?> createCourse(@RequestBody Course course) {
    repo.save(course);
    return ResponseEntity.ok(Map.of("message","Course saved"));
  }
}
```

---

## ✅ 2. Angular Frontend Enhancements

### 🛠️ Upload Files & Collect URLs

Adapt your existing upload logic to get both `presignedUrl` and `publicUrl`:

```ts
uploadToS3(file: File, filePath: string) {
  const ct = file.type;
  this.http.get<{presignedUrl:string, publicUrl:string}>(
    `/api/s3/generate-presigned?fileName=${filePath}&contentType=${ct}`
  ).subscribe(resp => {
    this.http.put(resp.presignedUrl, file, { headers: {'Content-Type': ct} })
      .subscribe(() => {
        this.uploadedPublicUrls.push(resp.publicUrl);
      });
  });
}
```

### 📝 Modify `createCourse()` to send comma-separated strings

```ts
createCourse() {
  const imagesCsv = this.uploadedPublicUrls
    .filter(url => url.includes('/image/'))
    .join(',');

  const videoUrl = this.uploadedPublicUrls
    .find(url => url.includes('/video/')) || '';

  const payload = {
    title: this.course.title,
    description: this.course.description,
    level: this.course.level,
    features: this.course.features.join(','),       // csv
    courseOutLine: this.course.courseOutLine,
    imageUrls: imagesCsv,
    videoUrl: videoUrl,
    creatorId: this.course.creator.id,
    categoryId: this.course.category.id
  };

  this.http.post(`${api.url}/courses/insertCourse`, payload, {
    headers: { Authorization:`Bearer ${localStorage.getItem('token')}` }
  }).subscribe({
    next: () => this.showCourseCreationSuccessModal = true,
    error: () => this.toastr.showError('Failed to create course')
  });
}
```

---

## 🔚 Summary Flow

| Step | Description                                                   |
| ---- | ------------------------------------------------------------- |
| 1    | User selects images + video                                   |
| 2    | Angular requests pre-signed URLs from Spring Boot             |
| 3    | Angular uploads files directly to S3                          |
| 4    | Angular constructs comma-separated public URLs string         |
| 5    | Angular posts final course form (with CSV strings) to backend |
| 6    | Spring Boot saves a **single-string** list in the DB          |

---

## ✅ Next Steps

* ✅ Add validation & error-handling (size/type limits)
* ✅ Add CORS policy for `api/s3`
* ✅ Secure bucket (private) and generate pre-signed GET URLs for video/image access later

Let me know if you'd like me to add:

* retrieval endpoints to split CSV fields,
* presigned GET URLs for private media,
* or examples for displaying comma-separated images properly!
