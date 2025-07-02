create-course-page.component.ts:301 
 Upload error: 
HttpErrorResponse {headers: _HttpHeaders, status: 200, statusText: 'OK', url: 'http://localhost:4200/api/s3/generate-presigned?fi…438717915_0_image%20(2).png&contentType=image/png', ok: false, …}
error
: 
{error: SyntaxError: Unexpected token '<', "<!DOCTYPE "... is not valid JSON at JSON.parse (<anonymous>…, text: '<!DOCTYPE html><html lang="en"><head>\n  \x3Cscript ty…:"c2104709656","r":1}]}}]}\x3C/script></body></html>'}
headers
: 
_HttpHeaders {headers: undefined, normalizedNames: Map(0), lazyUpdate: null, lazyInit: ƒ}
message
: 
"Http failure during parsing for http://localhost:4200/api/s3/generate-presigned?fileName=image/1751438717915_0_image%20(2).png&contentType=image/png"
name
: 
"HttpErrorResponse"
ok
: 
false
status
: 
200
statusText
: 
"OK"
type
: 
undefined
url
: 
"http://localhost:4200/api/s3/generate-presigned?fileName=image/1751438717915_0_image%20(2).png&contentType=image/png"
[[Prototype]]
: 
HttpResponseBase
(anonymous)	@	create-course-page.component.ts:301
Zone - Promise.then		
error	@	course.service.ts:112
Zone - XMLHttpRequest.addEventListener:load		
(anonymous)	@	course.service.ts:103
uploadToS3	@	course.service.ts:100
(anonymous)	@	create-course-page.component.ts:209
uploadAllMedia	@	create-course-page.component.ts:207
onSubmit	@	create-course-page.component.ts:283
CreateCoursePageComponent_Template_form_ngSubmit_18_listener	@	create-course-page.component.html:26




[
  {
    "AllowedHeaders": ["*"],
    "AllowedMethods": ["GET", "PUT", "POST", "DELETE"],
    "AllowedOrigins": ["http://localhost:4200", "https://yourdomain.com"],
    "ExposeHeaders": ["ETag"],
    "MaxAgeSeconds": 3000
  }
]
