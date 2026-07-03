package main.java;

public class URLMethod {
    private String url;
    private String method;

    public URLMethod(String url, String method) {
        this.url = url;
        this.method = method;
    }
    
    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        } 
        URLMethod urlMethod = (URLMethod) o;
        
        boolean urlEquals = false;
        if (this.url == null && urlMethod.url == null) {
            urlEquals = true; 
        }         
        if (this.url != null && this.url.equals(urlMethod.url)) {
            urlEquals = true;
        }
    
        boolean methodEquals = false;                    
        if (this.method == null && urlMethod.method == null) {
            methodEquals = true;
        }
        if (this.method != null && this.method.equals(urlMethod.method)) {
            methodEquals = true;
        }
    
        return urlEquals && methodEquals;
    }

     public int hashCode() {
         String combine = this.url + " " + this.method;
         return combine.hashCode();
     }
}