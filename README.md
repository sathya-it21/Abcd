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
 
