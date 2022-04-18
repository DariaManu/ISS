import java.io.Serializable;

public class Response implements Serializable {
    private ResponseType responseType;
    private Object data;

    private Response() {};

    public ResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static class Builder {
        private Response response = new Response();

        public Builder setResponseType(ResponseType responseType) {
            response.setResponseType(responseType);
            return this;
        }

        public Builder setData(Object data) {
            response.setData(data);
            return this;
        }

        public Response build() {
            return response;
        }
    }
}
