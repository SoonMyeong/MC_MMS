
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class MnsSocketClient {

	private static final Logger logger = LoggerFactory.getLogger(MnsSocketClient.class);

	public static void main(String[] args) {
		String req1 = "{\"geocasting_circle\":{\"srcMRN\":\"SP\",\"dstMRN\":\"*\",\"lat\":\"33.54995\",\"long\":\"176.72032\",\"radius\":\"400.0\"}}";
		String req2 = "{\"geocasting_polygon\":{\"srcMRN\":\"SP\",\"dstMRN\":\"*\",\"lat\":[\"34.79625\",\"43.315407\",\"4.439433\",\"7.548697\"],\"long\":[\"121.410484\",\"172.75629\",\"167.1882\",\"111.833565\"]}}";
		String req3 = "{\"unicasting\":{\"srcMRN\":\"SP\",\"dstMRN\":\"urn:mrn:smart:service:instance:mof:S10\",\"IPAddr\":\"127.0.0.1\"}}";
		String req4 = "Dump-MNS:";
		Socket mnsSocket = null;
		PrintWriter pw = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		String queryReply = null;
		try {
			// String modifiedSentence;

			mnsSocket = new Socket("127.0.0.1", 5855);
			mnsSocket.setSoTimeout(5000);
			pw = new PrintWriter(mnsSocket.getOutputStream());
			isr = new InputStreamReader(mnsSocket.getInputStream());
			br = new BufferedReader(isr);
			String inputLine = null;
			StringBuffer response = new StringBuffer();

			pw.println(req4);
			pw.flush();
			if (!mnsSocket.isOutputShutdown()) {
				mnsSocket.shutdownOutput();
			}

			while ((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}

			queryReply = response.toString();
			logger.debug("From MNS server= {}", queryReply);

		} catch (UnknownHostException e) {

			logger.error("unknown host");
		} catch (IOException e) {

			logger.error("io error");
		} finally {
			if (pw != null) {
				pw.close();
			}
			if (isr != null) {
				try {
					isr.close();
				} catch (IOException e) {
					logger.error("io error");
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					logger.error("io error");
				}
			}
			if (mnsSocket != null) {
				try {
					mnsSocket.close();
				} catch (IOException e) {
					logger.error("io error");
				}
			}
		}
		logger.info("result ================ {}", queryReply);
	}
}
