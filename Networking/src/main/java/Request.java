import java.io.Serializable;

public class Request implements Serializable {
    private RequestType requestType;
    private Object data;

    private Request() {};

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Request{" +
                "requestType=" + requestType +
                ", data=" + data +
                '}';
    }

    public static class Builder {
        private Request request = new Request();

        public Builder setRequestType(RequestType requestType) {
            request.setRequestType(requestType);
            return this;
        }

        public Builder setData(Object data) {
            request.setData(data);
            return this;
        }

        public Request build() {
            return request;
        }
    }
}
