import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import sun.misc.BASE64Encoder;
import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;


public class Captcha {
	final static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36";
	static String key = "13f8a1a960d6f9893833de9fb2fbd75f";
	private static Logger logger = Logger.getLogger(Captcha.class.getName());

	public String sendPost(URL href) throws Exception {
		StringBuffer response = null;
		String conStatus = null;
		URL captchaHref = null;
		while (conStatus == null) {
			try {
				captchaHref = href;
				BufferedImage img = ImageIO.read(captchaHref);
				String encodedCaptcha = encodeToString(img, "png");
				String url = "http://anti-captcha.com/in.php?";
				URL obj = new URL(url);
				HttpURLConnection con = (HttpURLConnection) obj
						.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("User-Agent", USER_AGENT);
				con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
				String image = URLEncoder.encode(encodedCaptcha, "UTF-8");
				key = URLEncoder.encode(key, "UTF-8");
				String urlParameters = "key=" + key + "&body=" + image
						+ "&method=base64";
				con.setDoOutput(true);

				DataOutputStream wr = new DataOutputStream(
						con.getOutputStream());
				wr.writeBytes(urlParameters);
				wr.flush();
				wr.close();
				con.setConnectTimeout(700);
				BufferedReader in = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				String inputLine;
				response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				conStatus = response.toString();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		String text = null;

		String answerKey = response.toString().substring(3,
				response.toString().length());
		if (response.toString().contains("OK|")) {
			text = getCaptchaText(answerKey);
			
			logger.info("Text" + text);
			text = text.substring(3, text.length());

		} else if (response.toString().contains("NO_SLOT")) {
			sendPost(captchaHref);
		}

		return text;
	}

	private String getCaptchaText(String imageKey) throws Exception {
		String url = "http://anti-captcha.com/res.php?";
		URL obj = new URL(url);
		String conStatus = null;
		while (conStatus == null) {
			try {
				HttpURLConnection con = (HttpURLConnection) obj
						.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("User-Agent", USER_AGENT);
				con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

				key = URLEncoder.encode(key, "UTF-8");
				String urlParameters = "key=" + key + "&action=get&id="
						+ imageKey;
				con.setDoOutput(true);

				DataOutputStream wr = new DataOutputStream(
						con.getOutputStream());
				wr.writeBytes(urlParameters);
				wr.flush();
				wr.close();
				con.setConnectTimeout(700);
				BufferedReader in = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				conStatus = response.toString();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		String captchaText = null;
		logger.info(conStatus);
		if (conStatus.toString().contains("CAPCHA_NOT_READY")) {
			do {
				Thread.sleep(5000);
				captchaText = getCaptchaText(imageKey);
				logger.info("in while"+captchaText);
			} while (captchaText==null);
			logger.info("After while"+captchaText);
			/*
			 * while(captchaText.contains("CAPCHA_NOT_READY")){
			 * captchaText=getCaptchaText(imageKey); }
			 */
			/*logger.info(conStatus);
			Thread.sleep(5100);
			captchaText = getCaptchaText(imageKey);*/
		} else if (conStatus.toString().contains("OK|")) {
			captchaText = conStatus;
			logger.info("ok" + conStatus);

		}
		return captchaText;
	}

	private String encodeToString(BufferedImage image, String type) {
		String imageString = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {
			ImageIO.write(image, type, bos);
			byte[] imageBytes = bos.toByteArray();

			BASE64Encoder encoder = new BASE64Encoder();
			imageString = encoder.encode(imageBytes);

			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imageString;
	}

}
