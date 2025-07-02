//dsfdsf
Perfect. Since you’ve shared your full `create-course-page.component.ts`, here's exactly how to integrate the `uploadToS3()` functionality and where to call it in this file.

---

## ✅ What to do

You need to:

### 1. Add a method in `course.service.ts` like this:

```ts
uploadToS3(file: File, filePath: string): Promise<string> {
  const contentType = file.type;
  return new Promise((resolve, reject) => {
    this.http.get<{ presignedUrl: string, publicUrl: string }>(
      `/api/s3/generate-presigned?fileName=${filePath}&contentType=${contentType}`
    ).subscribe({
      next: (response) => {
        this.http.put(response.presignedUrl, file, {
          headers: { 'Content-Type': contentType }
        }).subscribe({
          next: () => resolve(response.publicUrl),
          error: (err) => reject(err)
        });
      },
      error: (err) => reject(err)
    });
  });
}
```

> You already have the `courseService` injected in your component, so this is accessible.

---

### 2. Add this method in your component (`create-course-page.component.ts`):

```ts
uploadAllMedia(): Promise<void> {
  const uploadPromises: Promise<string>[] = [];

  const imageUploadPromises = this.newCourse.courseImages.map((img, i) => {
    const path = `image/${Date.now()}_${i}_${img.name}`;
    return this.courseService.uploadToS3(img, path);
  });

  uploadPromises.push(...imageUploadPromises);

  const videoPromise = this.newCourse.courseVideo
    ? this.courseService.uploadToS3(this.newCourse.courseVideo, `video/${Date.now()}_${this.newCourse.courseVideo.name}`)
    : Promise.resolve('');

  uploadPromises.push(videoPromise);

  return Promise.all(uploadPromises).then((urls) => {
    const videoUrl = urls.pop(); // Last one is the video
    const imageUrls = urls;

    this.course.imageUrl = imageUrls.join(',');
    this.course.videoUrl = videoUrl || '';
  });
}
```

---

### 3. Modify your `onSubmit()` method like this:

Replace this:

```ts
console.log(this.course);
this.courseService.insertCourse(this.course);
```

With this:

```ts
this.uploadAllMedia().then(() => {
  this.course.features = [...this.newCourse.features];
  this.course.courseOutLine = this.newCourse.courseContent;
  this.course.title = this.newCourse.title;
  this.course.description = this.newCourse.description;
  this.course.level = this.newCourse.level;
  this.course.category.id = this.categoryService.getCategoryId(this.newCourse.category);

  this.courseService.insertCourse(this.course).subscribe({
    next: () => {
      this.createdCourseTitle = this.course.title;
      this.showCourseCreationSuccessModal = true;
    },
    error: (err) => {
      this.toastr.showError('Failed to create course. ' + err.message);
    }
  });
}).catch((err) => {
  this.toastr.showError('File upload failed: ' + err.message);
});
```

---

### ✅ Summary of Placement

| Function            | File                              | Inside                              |
| ------------------- | --------------------------------- | ----------------------------------- |
| `uploadToS3()`      | `course.service.ts`               | `CourseService` class               |
| `uploadAllMedia()`  | `create-course-page.component.ts` | `CreateCoursePageComponent` class   |
| Modify `onSubmit()` | `create-course-page.component.ts` | Wrap upload and call `insertCourse` |

---

Let me know if you want to show upload progress or want to support retry/failure UI.
