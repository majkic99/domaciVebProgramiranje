package model;

public class Response {

    private Result result;

    public Response() {
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

	@Override
	public String toString() {
		return "Response [result=" + result.toString() + "]";
	}
    
    
}
