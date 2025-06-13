Course model:

package com.ctrlaltdefeat.Bartr.models;

import java.util.Date;

public class Course {
	private String id;
	private String title;
	private String description;
	private String category_id;
	private String creator_id;
	private Date created_at;
	private String level;
	
	//Getters and Setters
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCategory_id() {
		return category_id;
	}
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	public String getCreator_id() {
		return creator_id;
	}
	public void setCreator_id(String creator_id) {
		this.creator_id = creator_id;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	
}


Course controllers:

package com.ctrlaltdefeat.Bartr.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ctrlaltdefeat.Bartr.services.CourseService;
import com.ctrlaltdefeat.Bartr.models.Course;;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    
    private final CourseService courseService;

   public CourseController(CourseService courseService) {
       this.courseService = courseService;
   }
    @PostMapping
    public Course createCourse(@RequestBody Course course){
        return courseService.createCourse(course);
    }


    @GetMapping
    public List<Course> getAllCourse() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public Course getCourseById(@PathVariable String id)  {
        return courseService.getCourseById(id);
    }

    @PutMapping("/{id}")
    public Course updateCourse(@PathVariable String id, @RequestBody Course course){
        return courseService.updateCourse(id, course);
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable String id) {
        courseService.deleteCourse(id);
    }

}


Course services:

package com.ctrlaltdefeat.Bartr.services;
import com.ctrlaltdefeat.Bartr.models.Course;
import com.ctrlaltdefeat.Bartr.repository.CourseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class CourseService {
   private final CourseRepository courseRepository;
   private final ObjectMapper objectMapper =new ObjectMapper();
   public CourseService(CourseRepository courseRepository) {
       this.courseRepository = courseRepository;
   }
   public Course createCourse(Course course) {
        Map<String,Object> data = objectMapper.convertValue(course, Map.class);
       return courseRepository.createDocument(data);
   }
   public Course getCourseById(String id) {
       return courseRepository.getDocument(id);
   }
   public List<Course> getAllCourses() {
       return courseRepository.listDocuments();
   }
   public Course updateCourse(String id, Course course) {
    Map<String,Object> data = objectMapper.convertValue(course, Map.class);
       return courseRepository.updateDocument(id, data);
   }
   public void deleteCourse(String id) {
        courseRepository.deleteDocument(id);
   }
}     

Course repository:

package com.ctrlaltdefeat.Bartr.repository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.ctrlaltdefeat.Bartr.models.Course;
@Repository
public class CourseRepository extends AppwriteRestRepository<Course> {
   @Value("${appwrite.collection.courses}")
   private String collectionId;
   public CourseRepository(RestTemplate restTemplate) {
       super(restTemplate);
   }
   @Override
   protected String getCollectionId() {
       return collectionId;
   }

   @Override
   protected Class<Course> getEntityClass() {
       return Course.class;
   }
}


AppwriteRestRepository:


package com.ctrlaltdefeat.Bartr.repository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public abstract class AppwriteRestRepository<T> {
   protected final RestTemplate restTemplate;
   protected final ObjectMapper objectMapper = new ObjectMapper();
   @Value("${appwrite.endpoint}")
   protected String baseUrl;
   @Value("${appwrite.project.id}")
   protected String projectId;
   @Value("${appwrite.api.key}")
   protected String apiKey;
   @Value("${appwrite.database.id}")
   protected String databaseId;
   public AppwriteRestRepository(RestTemplate restTemplate) {
       this.restTemplate = restTemplate;
   }
   protected abstract String getCollectionId();
   protected abstract Class<T> getEntityClass();
   public T createDocument(Map<String, Object> data) {
       HttpHeaders headers = buildHeaders();
       Map<String, Object> body = Map.of("documentId", "unique()", "data", data);
       HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
       ResponseEntity<Map> response = restTemplate.postForEntity(
               baseUrl + "/databases/" + databaseId + "/collections/" + getCollectionId() + "/documents",
               request,
               Map.class
       );
       Map<String, Object> documentData = response.getBody();
       System.out.println(documentData);
       documentData.keySet().removeIf(key -> key.startsWith("$"));
       return objectMapper.convertValue(documentData, getEntityClass());
   }
   public T getDocument(String id) {
       HttpHeaders headers = buildHeaders();
       HttpEntity<Void> request = new HttpEntity<>(headers);
       ResponseEntity<Map> response = restTemplate.exchange(
               baseUrl + "/databases/" + databaseId + "/collections/" + getCollectionId() + "/documents/" + id,
               HttpMethod.GET,
               request,
               Map.class
       );
       Map<String, Object> documentData = response.getBody();
       System.out.println(documentData);
       documentData.keySet().removeIf(key -> key.startsWith("$"));
       return objectMapper.convertValue(documentData, getEntityClass());
   }
   public List<T> listDocuments() {
       HttpHeaders headers = buildHeaders();
       HttpEntity<Void> request = new HttpEntity<>(headers);
       ResponseEntity<Map> response = restTemplate.exchange(
               baseUrl + "/databases/" + databaseId + "/collections/" + getCollectionId() + "/documents",
               HttpMethod.GET,
               request,
               Map.class
       );
       List<Map<String, Object>> documents = (List<Map<String, Object>>) response.getBody().get("documents");
       System.out.println(documents);

       return documents.stream()
               .map(doc -> {
                doc.keySet().removeIf(key -> key.startsWith("$"));
                return objectMapper.convertValue(doc, getEntityClass());
                })
               .collect(Collectors.toList());
   }
   public T updateDocument(String id, Map<String, Object> data) {
    try {
        HttpHeaders headers = buildHeaders();
        Map<String, Object> body = Map.of("data", data);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        
        ResponseEntity<Map> response = restTemplate.exchange(
            baseUrl + "/databases/" + databaseId + "/collections/" + getCollectionId() + "/documents/" + id,
            HttpMethod.PATCH,
            request,
            Map.class
        );
        
        // Handle success
        Map<String, Object> documentData = response.getBody();
        System.out.println(documentData);
        documentData.keySet().removeIf(key -> key.startsWith("$"));
        return objectMapper.convertValue(documentData, getEntityClass());
    } catch (HttpClientErrorException e) {
        // Client-side error (4xx)
        System.err.println("Client error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        throw e;
    } catch (HttpServerErrorException e) {
        // Server-side error (5xx)
        System.err.println("Server error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        throw e;
    } catch (Exception e) {
        // Other errors
        System.err.println("Unknown error: " + e.getMessage());
        throw e;
    }
}
   public void deleteDocument(String id) {
       HttpHeaders headers = buildHeaders();
       HttpEntity<Void> request = new HttpEntity<>(headers);
       restTemplate.exchange(
               baseUrl + "/databases/" + databaseId + "/collections/" + getCollectionId() + "/documents/" + id,
               HttpMethod.DELETE,
               request,
               Void.class
       );
   }
   private HttpHeaders buildHeaders() {
       HttpHeaders headers = new HttpHeaders();
       headers.set("X-Appwrite-Project", projectId);
       headers.set("X-Appwrite-Key", apiKey);
       headers.setContentType(MediaType.APPLICATION_JSON);
       return headers;
   }
}


this is database structure
courses



Documents
Attributes
Indexes
Activity
Usage
Settings
Attributes

Create attribute
Key
Type
Default value
title
required
string

-

description
required
string

-

created_at
datetime

-

level
required
string

-

category_id
relationship with category_id

-

creator_id
relationship with creator_id

-

when i post http://localhost:8081/api/courses
{
    "title": "Java programming",
    "description": "desc",
    "level": "Beginner",
    "creator_id": "68494c4a6d51e1783fea",
    "category_id": "684824e45633a2ac1f0c"
    
}

it shows error  "{"message":"Invalid document structure: Unknown attribute: \"id\"","code":400,"type":"document_invalid_structure","version":"1.7.4"}"] with root cause
