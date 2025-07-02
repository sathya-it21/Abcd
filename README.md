//type.ts
export interface Courses {
  id: number,
  title: string,
  description: string,
  level: string,
  features: string[],
  courseOutLine: string,
  price: number,
  imageUrl: string,
  videoUrl: string,
  enrolledUser: number,
  category: Category,
  creator: User,
  createdAt?: string,


}


create-course-page.component.ts
import { FormsModule } from '@angular/forms';
import { Component, NgModule, OnInit } from '@angular/core'; // NgModule is usually in app.module.ts, remove if standalone
import { NavbarComponentComponent } from "../navbar-component/navbar-component.component";
import { NgFor, NgIf } from '@angular/common'; // Already imported by standalone
import { Router } from '@angular/router';
import { GenericSuccessModalComponent } from "../generic-success-modal/generic-success-modal.component";
import { Courses,Category,User } from '../types';
import { CourseService } from '../services/course.service';
import { CategoryService } from '../services/category.service';
import { UserService } from '../services/user.service';
import { ToastrService } from '../toastr/toastr.service';

interface Course {
  title: string;
  description: string;
  category: string;
  // xpPrice: number; // Keeping commented out as per your code
  courseImages: File[];
  level: string;
  features: string[]; // Array of strings for features
  courseContent: string; // Placeholder for content, could be richer in a real app
  courseVideo: File | null;
}

@Component({
  selector: 'app-create-course-page',
  // If this is a standalone component, you don't need @NgModule decorator here.
  // The 'imports' array within @Component is for standalone components.
  // If it's part of a module, the 'imports' here should be in the module's @NgModule.
  // Assuming it's standalone based on recent Angular conventions.
  standalone: true, // Add this if it's not already defined elsewhere (e.g., in module file if not standalone)
  imports: [NavbarComponentComponent, NgIf, NgFor, FormsModule, GenericSuccessModalComponent],
  templateUrl: './create-course-page.component.html',
  styleUrl: './create-course-page.component.css'
})
export class CreateCoursePageComponent implements OnInit{

  currentStep: number = 1;

  newCourse: Course = {
    title: '',
    description: '',
    category: '', // Default empty
    // xpPrice: 0, // Keeping commented out as per your code
    level: '',
    courseImages: [],
    features: [''], // Start with one empty field
    courseContent: '',
    courseVideo: null
  };
  course:Courses={
    id: 0,
    title: '',
    description: '',
    level: '',
    features: [],
    courseOutLine: '',
    price: 0,
    imageUrl: '',
    videoUrl: '',
    enrolledUser: 0,
    category: {
      id: 0
    },
    creator: {
      id: 0,
      username: '',
      skills: ''
    }
  }
  categories: string[] = [
    'Information Technology',
    'Music',
    'Language',
    'Drawing',
  ];

  level: string[] = [
    'Beginner',
    'Intermediate',
    'Advanced'
  ];

  // <--- NEW: Property to control success modal visibility
  showCourseCreationSuccessModal: boolean = false;
  createdCourseTitle: string = '';
   // To pass to the modal

  constructor(private router: Router,
              private courseService:CourseService,
              private categoryService:CategoryService,
              private userService:UserService,
              private toastr: ToastrService,
            ) {}
  ngOnInit(): void {
    this.categories=this.categoryService.getCategoryNames();
    console.log(this.categories);
    this.userService.getByUserName(localStorage.getItem('username')||'').subscribe({
      next: (userData: User) => {
        this.course.creator.id = userData.id;
      }
    });
  }




  // === NEW: trackBy function for ngFor ===
  trackByFeature(index: number, feature: string): number {
    return index; // Uniquely identifies each item by its index
  }

  // === Step Navigation Methods ===
  goToNextStep(): void {
    // Validate current step before moving to the next
    if (this.currentStep === 1) {
      // Adjusted validation based on your provided interface and common sense for required fields
      if (!this.course.title || !this.course.description || !this.course.category || !this.course.level || !this.course.courseOutLine) {
        // alert('Please fill in all required fields for Basic Details (Title, Description, Category, Level, Course Content Outline).');
        this.toastr.showError('Please fill in all required fields for Basic Details (Title, Description, Category, Level, Course Content Outline).')
        return;
      }
      if (this.course.features.some(f => !f || f.trim() === '')) {
        //  alert('Please ensure all course features are filled or remove empty ones.');
         this.toastr.showError('Please ensure all course features are filled or remove empty ones.');
         return;
      }
      if (this.newCourse.courseImages.length === 0) {
        // alert('Please upload at least one course image.');
        this.toastr.showError('Please upload at least one course image.');
        return;
      }
    }
    this.currentStep++;
  }

  goToPreviousStep(): void {
    this.currentStep--;
  }

  onVideoSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      if (file.type.startsWith('video/')) {
        this.newCourse.courseVideo = file;
        console.log('Selected video:', this.newCourse.courseVideo.name);
      } else {
        // alert('Only video files are allowed.');
        this.toastr.showError('Only video files are allowed.');
        this.newCourse.courseVideo = null;
        input.value = ''; // Clear the input if not a video
      }
    } else {
      this.newCourse.courseVideo = null;
    }
  }

  onImageSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files) {
      this.newCourse.courseImages = []; // Clear existing images on new selection
      // Add up to 4 selected files to the array
      for (let i = 0; i < input.files.length && i < 4; i++) {
        const file = input.files[i];
        if (file.type.startsWith('image/')) {
            this.newCourse.courseImages.push(file); // Store File object
        } else {
            // alert(`File "${file.name}" is not an image and will be skipped.`);
            this.toastr.showError(`File "${file.name}" is not an image and will be skipped.`)
        }
      }
      console.log('Selected images:', this.newCourse.courseImages.map(f => f.name));
      // No need to clear input.value if you want to display selected file names and manage them.
      // If you want to force re-selection: input.value = '';
    }
  }

  // Remove a specific image by its index
  removeImage(index: number): void {
    if (index > -1 && index < this.newCourse.courseImages.length) {
      this.newCourse.courseImages.splice(index, 1);
    }
  }

  // Add a new feature field
  addFeatures(): void {
    if (this.newCourse.features.length < 6) { // Limit to 6 features as an example
      this.newCourse.features.push('');
    } else {
        // alert('You can add a maximum of 6 features.');
        this.toastr.showError('You can add a maximum of 6 features.')
    }
  }

  // Remove a feature field
  removeFeatures(index: number): void {
    if (this.newCourse.features.length > 1) { // Ensure at least one feature input remains
      this.newCourse.features.splice(index, 1);
    }
  }

  // Handle course submission
  onSubmit(): void {

    if (!this.course.title || this.course.title.trim() === '') {
      // alert('Course title cannot be empty.');
      this.toastr.showError('Course title cannot be empty.')
      this.currentStep = 1; // Go back to step 1
      return;
    }

    // Final validation for Step 2
    if (this.currentStep === 2) {
      if (!this.newCourse.courseVideo) {
        // alert('Please upload the main course video.');
        this.toastr.showError('Please upload the main course video.');
        return;
      }
    }
    console.log(this.course);
    this.courseService.insertCourse(this.course);
    console.log('New Course Data:', this.newCourse);

    // Simulate API call for course creation
    // In a real application, you would send this.newCourse to your backend service
    // e.g., this.courseService.createCourse(this.newCourse).subscribe(...)

    // alert('Course created successfully! (Simulated)'); // This alert will now be replaced by the modal.

    // --- MODIFIED SECTION ---
    // Capture the title for the modal message before resetting
    this.createdCourseTitle = this.course.title;

    // Show the success modal
    this.showCourseCreationSuccessModal = true;

    // DO NOT reset form here. Reset happens when modal is closed.
    // this.newCourse = { ... };
    // this.currentStep = 1;
  }

  // <--- NEW / MODIFIED: Handlers for Course Creation Success Modal ---
  onCourseCreationModalClose(): void {
    console.log('Course creation success modal closed.');
    this.showCourseCreationSuccessModal = false; // Close the modal
    this.resetForm(); // Reset form when modal is explicitly closed
    // Optionally navigate or refresh data here if not handled by primary action
  }

  onViewCreatedCourse(): void {
    console.log('Primary action from Course Creation Success Modal: Navigating to My Courses.');
    this.showCourseCreationSuccessModal = false; // Close the modal
    this.resetForm(); // Reset form when modal primary action is taken
    // Example navigation to the user's "My Courses" section
    this.router.navigate(['/profile'], { queryParams: { tab: 'My Courses' } });
  }

  // New utility method to reset the form state
  resetForm(): void {
    this.newCourse = {
      title: '',
      description: '',
      category: '',
      // xpPrice: 0,
      level: '',
      courseImages: [],
      features: [''], // Reset with one empty feature
      courseContent: '',
      courseVideo: null
    };
    this.currentStep = 1; // Reset to the first step
    this.createdCourseTitle = ''; // Clear the captured title
  }
}


// src/app/services/course.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http'; // Import HttpClient
import { Observable } from 'rxjs'; // Import Observable
import { map } from 'rxjs/operators'; // Import map operator for transformations
import { api } from '../api'; // Assuming 'api' is a constant with your base URL
import { Courses } from '../types'; // Import Courses interface
import axios from 'axios';
// Remove axios as we are switching to HttpClient
// import axios from 'axios';

@Injectable({
  providedIn: 'root'
})
export class CourseService {
  private readonly defUrl: string = '/api/courses';
  private readonly baseUrl: string; // To store the combined base URL

  constructor(private http: HttpClient) {
    this.baseUrl = api.url + this.defUrl; // Combine api.url with defUrl in constructor
  }

  /**
   * Fetches all courses from the backend.
   * @returns An Observable that emits an array of Courses.
   */
  getAllCourses(): Observable<Courses[]> {
    return this.http.get<Courses[]>(this.baseUrl);
  }

  /**
   * Fetches courses created by a specific creator.
   * @param creatorId The ID of the creator.
   * @returns An Observable that emits an array of Courses.
   */
  getCoursesByCreator(creatorId: number): Observable<Courses[]> {
    return this.http.get<Courses[]>(`${this.baseUrl}/creator/${creatorId}`);
  }

  /**
   * Fetches courses belonging to a specific category.
   * @param categoryId The ID of the category.
   * @returns An Observable that emits an array of Courses.
   */
  getCoursesByCategory(categoryId: number): Observable<Courses[]> {
    return this.http.get<Courses[]>(`${this.baseUrl}/category/${categoryId}`);
  }

  /**
   * Fetches a single course by its ID and processes its features string.
   * @param courseId The ID of the course.
   * @returns An Observable that emits the Course object with features as an array.
   */
  getCourseById(courseId: number): Observable<Courses> {
    return this.http.get<Courses>(`${this.baseUrl}/${courseId}`).pipe(
      map(course => {
        // Assuming course.features is a comma-separated string from the backend
        // We'll split it into an array here.
        if (typeof course.features === 'string') {
          // Type assertion to ensure 'features' can be assigned to 'string[]'
          // This requires your Courses interface to have `features: string | string[];`
          // or `features: any;` or better, adjust the backend to send it as array if possible
          (course.features as any) = (course.features as string).split(',').map(f => f.trim());
        }
        return course;
      })
    );
  }

  /**
   * Deletes a course by its ID.
   * The component calling this method should handle success/error messages.
   * @param courseId The ID of the course to delete.
   * @returns An Observable that emits the backend response (or void if no content).
   */
  deleteCourse(courseId: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/deleteCourse/${courseId}`);
  }

  /**
   * Updates an existing course.
   * The component calling this method should handle success/error messages.
   * @param course The Course object with updated data.
   * @returns An Observable that emits the backend response (or void if no content).
   */
  updateCourse(course: Courses): Observable<any> {
    return this.http.put(`${this.baseUrl}/updateCourse/${course.id}`, course);
  }

  /**
   * Inserts a new course.
   * The component calling this method should handle success/error messages.
   * @param course The Course object to insert.
   * @returns An Observable that emits the backend response (or void if no content).
   */
  insertCourse(course: Courses):void {
    let a:any={
      title:course.title,
      description:course.description,
      level:course.level,
      features:course.features.join(","),
      courseOutLine:course.courseOutLine,
      category: course.category,
      creator: course.creator
    };
    axios.post(`${this.baseUrl}/insertCourse`,a,{headers:{Authorization:`Bearer ${localStorage.getItem('token')}`}})
    .then(function(resp){
      console.log("course created successfully");
    });
  }
 
}

//create-course-page.component.html

<div class="min-h-screen flex flex-col bg-gray-100 font-variable text-gray-800">
  <app-navbar-component></app-navbar-component>
 
  <main class="flex-grow py-8 px-4 md:px-8 max-w-4xl mx-auto w-full">
    <div class="bg-white p-6 md:p-8 rounded-lg shadow-xl border border-gray-200">
      <h1 class="text-4xl font-konkhmer text-gray-800 mb-6 pb-4 border-b border-gray-200">Create New Course</h1>
 
      <div class="mb-8 mx-10 flex justify-center items-center space-x-4">
        <div class="flex flex-col items-center">
          <div [class.bg-green-500]="currentStep >= 1" [class.bg-gray-300]="currentStep < 1"
               class="w-12 h-12 rounded-full flex items-center justify-center text-white font-bold transition-colors duration-300">
            1
          </div>
          <span class="text-sm mt-2 text-gray-600">Basic Details</span>
        </div>
        <div class="flex-grow h-0.5 bg-gray-300 transition-colors duration-300"></div>
        <div class="flex flex-col items-center">
          <div [class.bg-green-500]="currentStep >= 2" [class.bg-gray-300]="currentStep < 2"
               class="w-12 h-12 rounded-full flex items-center justify-center text-white font-bold transition-colors duration-300">
            2
          </div>
          <span class="text-sm mt-2 text-gray-600">Course Content</span>
        </div>
      </div>
 
      <form (ngSubmit)="onSubmit()" class="space-y-6">
 
        <div *ngIf="currentStep === 1" class="space-y-6">
          <div>
            <label for="courseTitle" class="block text-gray-700 text-sm font-semibold mb-2">Course Title <span class="text-red-500">*</span></label>
            <input type="text" id="courseTitle" name="courseTitle"
                   [(ngModel)]="course.title" required
                   placeholder="e.g., Mastering React Hooks"
                   class="w-full p-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500">
          </div>
 
          <div>
            <label for="courseDescription" class="block text-gray-700 text-sm font-semibold mb-2">Course Description <span class="text-red-500">*</span></label>
            <textarea id="courseDescription" name="courseDescription"
                      [(ngModel)]="course.description" required rows="4"
                      placeholder="Provide a detailed description of what your course covers."
                      class="w-full p-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"></textarea>
          </div>
 
          <div>
            <label for="courseCategory" class="block text-gray-700 text-sm font-semibold mb-2">Category <span class="text-red-500">*</span></label>
            <select id="courseCategory" name="courseCategory"
                    [(ngModel)]="course.category.id" required
                    class="w-full p-3 border border-gray-300 rounded-md bg-white focus:outline-none focus:ring-2 focus:ring-green-500">
              <option value="" disabled>Select a category</option>
              <option *ngFor="let cat of categories,let i=index" [value]="i+1">{{ cat }}</option>
            </select>
          </div>
 
          <div>
            <label for="courseImages" class="block text-gray-700 text-sm font-semibold mb-2">Course Images (Up to 4) <span class="text-red-500">*</span></label>
            <input type="file" id="courseImages" name="courseImages"
                   (change)="onImageSelected($event)" accept="image/*" multiple
                   [disabled]="newCourse.courseImages.length >= 4"
                   class="block w-full text-sm text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded-md file:border-0 file:text-sm file:font-semibold file:bg-green-50 file:text-green-700 hover:file:bg-green-100 disabled:opacity-50 disabled:cursor-not-allowed">
            <p class="mt-2 text-sm text-gray-600">
              {{ newCourse.courseImages.length }} of 4 images selected.
              <span *ngIf="newCourse.courseImages.length < 4"> (Select more or replace)</span>
            </p>
 
            <div *ngIf="newCourse.courseImages.length > 0" class="mt-4 grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 gap-4">
              <div *ngFor="let img of newCourse.courseImages; let i = index" class="relative group">
                <img [src]="img" alt="Course Image {{i+1}}" class="w-full h-32 object-cover rounded-md border border-gray-300">
                <button (click)="removeImage(i)" type="button"
                        class="absolute top-1 right-1 bg-red-500 text-white rounded-full p-1.5 opacity-0 group-hover:opacity-100 transition-opacity duration-200">
                  <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path></svg>
                </button>
                <p class="text-xs text-gray-600 mt-1 truncate" title="{{ img.name }}">{{ img.name }}</p>
              </div>
            </div>
          </div>
 
          <div>
            <label for="courseLevel" class="block text-gray-700 text-sm font-semibold mb-2">Level <span class="text-red-500">*</span></label>
            <select id="courseLevel" name="courseLevel"
                    [(ngModel)]="course.level" required
                    class="w-full p-3 border border-gray-300 rounded-md bg-white focus:outline-none focus:ring-2 focus:ring-green-500">
              <option value="" disabled>Select course level</option>
              <option *ngFor="let lvl of level" [value]="lvl">{{ lvl }}</option>
            </select>
          </div>
 
          <div>
            <label class="block text-gray-700 text-sm font-semibold mb-2">Features <span class="text-red-500">*</span></label>
            <div *ngFor="let item of newCourse.features; let i = index; trackBy: trackByFeature" class="flex items-center space-x-2 mb-2">
              <input type="text" [(ngModel)]="course.features[i]"
                      name="featureItem_{{i}}" placeholder="e.g., Learn responsive web design"
                      class="flex-grow p-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500">
              <button *ngIf="newCourse.features.length > 1" (click)="removeFeatures(i)" type="button"
                      class="p-2 text-red-500 hover:text-red-700 rounded-full focus:outline-none">
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"></path></svg>
              </button>
            </div>
            <button (click)="addFeatures()" type="button"
                    class="mt-2 px-4 py-2 bg-gray-200 text-gray-700 rounded-md text-sm hover:bg-gray-300 transition-colors flex items-center">
              <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m0 0H6"></path></svg>
              Add another
            </button>
          </div>
 
          <div>
            <label for="courseContent" class="block text-gray-700 text-sm font-semibold mb-2">Course Content Outline <span class="text-red-500">*</span></label>
            <textarea id="courseContent" name="courseContent"
                      [(ngModel)]="course.courseOutLine" rows="6" required
                      placeholder="Outline your course modules, topics, and lessons here. This is the textual overview of your course."
                      class="w-full p-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"></textarea>
          </div>
 
          <div class="pt-4 border-t border-gray-200 flex justify-end">
            <button type="button" (click)="goToNextStep()"
                    class="px-6 py-3 bg-green-600 text-white font-semibold rounded-md shadow-md hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500 focus:ring-offset-2 flex items-center">
              Next Step
              <svg class="w-5 h-5 ml-2" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 8l4 4m0 0l-4 4m4-4H3"></path></svg>
            </button>
          </div>
        </div>
 
        <div *ngIf="currentStep === 2" class="space-y-6">
          <h2 class="text-2xl font-bold text-gray-800 mb-4">Upload Course Video Content</h2>
 
          <div>
            <label for="courseVideo" class="block text-gray-700 text-sm font-semibold mb-2">Main Course Video <span class="text-red-500">*</span></label>
            <input type="file" id="courseVideo" name="courseVideo"
                   (change)="onVideoSelected($event)" accept="video/*" required
                   class="block w-full text-sm text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded-md file:border-0 file:text-sm file:font-semibold file:bg-green-50 file:text-green-700 hover:file:bg-green-100">
            <p *ngIf="newCourse.courseVideo" class="mt-2 text-sm text-gray-600">Selected: {{ newCourse.courseVideo.name }}</p>
            <p *ngIf="!newCourse.courseVideo" class="mt-2 text-sm text-gray-500">Please upload the primary video file for your course.</p>
          </div>
 
          <div class="pt-4 border-t border-gray-200 flex justify-between">
            <button type="button" (click)="goToPreviousStep()"
                    class="px-6 py-3 bg-gray-200 text-gray-700 font-semibold rounded-md shadow-md hover:bg-gray-300 focus:outline-none focus:ring-2 focus:ring-gray-400 focus:ring-offset-2 flex items-center">
              <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 16l-4-4m0 0l4-4m-4 4h18"></path></svg>
              Previous
            </button>
            <button type="submit"
                    class="px-6 py-3 bg-green-600 text-white font-semibold rounded-md shadow-md hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500 focus:ring-offset-2">
              Create Course
            </button>
          </div>
        </div>
 
      </form>
    </div>
  </main>
</div>
 
<app-generic-success-modal
  *ngIf="showCourseCreationSuccessModal"
  title="Course Created Successfully!"
  [message]="'Your course &quot;' + createdCourseTitle + '&quot; has been successfully created. You can now manage its content and make it available to students.'"
  primaryButtonText="View My Courses"
  secondaryButtonText="Create Another Course"
  (primaryAction)="onViewCreatedCourse()"
  (close)="onCourseCreationModalClose()"
></app-generic-success-modal>

this is how it is in the frontend using this give full code with the backend for uplading to s3 and make sure to use only a string not list with comma seperated values
