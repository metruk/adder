import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class CaptchaJackson {
	
	String error_code;
	String errorMessage;
	List requestParams;
	String captchaSid;
	String captchaImg;
	public String getErrorCode() {
		return error_code;
	}
	public void setErrorCode(String errorCode) {
		this.error_code = error_code;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public List getRequestParams() {
		return requestParams;
	}
	public void setRequestParams(List requestParams) {
		this.requestParams = requestParams;
	}
	public String getCaptchaSid() {
		return captchaSid;
	}
	public void setCaptchaSid(String captchaSid) {
		this.captchaSid = captchaSid;
	}
	public String getCaptchaImg() {
		return captchaImg;
	}
	public void setCaptchaImg(String captchaImg) {
		this.captchaImg = captchaImg;
	}
	@Override
	public String toString() {
		return "CaptchaJackson [errorCode=" + error_code + ", errorMessage="
				+ errorMessage + ", requestParams=" + requestParams
				+ ", captchaSid=" + captchaSid + ", captchaImg=" + captchaImg
				+ "]";
	}
	
}
