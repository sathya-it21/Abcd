

uploadToS3(file: File, filePath: string): Promise<string> {
    const contentType = file.type;
    let headers = new HttpHeaders();
    headers= headers.set('Authorization', `Bearer ${localStorage.getItem('token')}`);
    let params = new HttpParams();
    params = params.append('fileName', filePath.toString());
    params = params.append('contentType', file.type.toString());

    return new Promise((resolve, reject) => {
      this.http.get<{ presignedUrl: string, publicUrl: string }>(
        `${api.url}/api/s3/generate-presigned`,{headers:headers,params:params}
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
