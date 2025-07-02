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
