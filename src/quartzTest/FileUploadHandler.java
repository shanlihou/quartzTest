package quartzTest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class FileUploadHandler extends IoHandlerAdapter {

	private BufferedOutputStream out;

	private static final Log log = LogFactory.getLog(FileUploadHandler.class);

	public void sessionOpened(IoSession session) throws Exception {
		System.out.println("server open");
	}

	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		System.out.println("exception");
		session.close(true);
		System.out.println(cause.getMessage());
		cause.printStackTrace();
		super.exceptionCaught(session, cause);
	}
	public void messageReceived(IoSession session, Object message) {
		System.out.println("server received");

		String str = message.toString();
		System.out.println("the message received is[" + str + "]");
		JSONObject jsonArray3 = JSONObject.fromObject(str);
		System.out.println(jsonArray3.getInt("req_type"));
		String strRet = HandleRequest.getInstance().handleJson(jsonArray3);
		session.write(strRet);
		/*
		try {
			if (message instanceof FileUploadRequest) {
				// FileUploadRequest 为传递过程中使用的DO。
				FileUploadRequest request = (FileUploadRequest) message;
				System.out.println(request.getFilename());
				if (out == null) {
					// 新建一个文件输入对象BufferedOutputStream，随便定义新文件的位置
					out = new BufferedOutputStream(new FileOutputStream(
							"D:/log/" + request.getFilename()));
					out.write(request.getFileContent());
				} else {
					out.write(request.getFileContent());
				}
				count += request.getFileContent().length;

			} else if (message instanceof String) {
				if (((String) message).equals("finish")) {
					System.out.println("size is" + count);
					// 这里是进行文件传输后，要进行flush和close否则传递的文件不完整。
					out.flush();
					out.close();
					// 回执客户端信息，上传文件成功
					session.write("success");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}

	public void sessionClosed(IoSession session) throws Exception {
		System.out.println("server session close");
	}
}