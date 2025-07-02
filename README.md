onSubmit(): void {
  if (!this.newCourse.title || this.newCourse.title.trim() === '') {
    this.toastr.showError('Course title cannot be empty.');
    this.currentStep = 1;
    return;
  }

  if (this.currentStep === 2 && !this.newCourse.courseVideo) {
    this.toastr.showError('Please upload the main course video.');
    return;
  }

  // Upload to S3 first
  this.uploadAllMedia().then(() => {
    // Prepare course object for backend
    const coursePayload = {
      title: this.newCourse.title,
      description: this.newCourse.description,
      level: this.newCourse.level,
      features: this.newCourse.features.join(","),
      courseOutLine: this.newCourse.courseContent,
      imageUrl: this.course.imageUrl,
      videoUrl: this.course.videoUrl,
      category: { name: this.newCourse.category },  // assuming your backend accepts category name
      creator: { id: this.course.creator.id }
    };

    this.courseService.insertCourse(coursePayload); // your service already logs success
    this.createdCourseTitle = this.newCourse.title;
    this.showCourseCreationSuccessModal = true;

  }).catch((err) => {
    console.error('Upload error:', err);
    this.toastr.showError('File upload failed. ' + (err?.message || ''));
  });
}
