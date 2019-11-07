

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

class NettyMessageSenderForMcmPostDelete {

	private static final Logger logger = LoggerFactory.getLogger(NettyMessageSenderForMcmPostDelete.class);
	
	public static class MessageBlockingQue {
		public static BlockingQueue<String> queue;// = new LinkedBlockingQueue<String>();
		
		
		MessageBlockingQue() {
			queue = new LinkedBlockingQueue<String>();
		}

		public static void setQueue(BlockingQueue<String> msg) {
			queue = msg;
		}

		public static BlockingQueue<String> getQueue() {
			return queue;
		}
	}
	
	public static class MnsClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
		private String req;
			public MnsClientHandler(String req) {
				this.req = req;
		}

			@Override
			public void channelActive(ChannelHandlerContext ctx) {
				logger.debug("============req\r{}\r",req);
				ctx.writeAndFlush(Unpooled.copiedBuffer(req, CharsetUtil.UTF_8));

			}

			@Override
			public void channelRead0(ChannelHandlerContext ctx, ByteBuf in) {
				String res = "";
				
				res = in.toString(CharsetUtil.UTF_8);
				logger.debug("\r\r\rClient received: {}\r\r", res);

				try {
					MessageBlockingQue.getQueue().put(res);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				logger.debug("\rlength {}", res.length());
			}

			@Override
			public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
				cause.printStackTrace();
				ctx.close();
			}
	}
	
	public static void main(String [] args) {
		new MessageBlockingQue();
		Map<String, String> bodyMap = new HashMap<String, String>();
		Map<String, String> headMap = new HashMap<String, String>();
		
		headMap.put("MMS-MRN", "urn:mrn:smart-navi:device:mms1");
		headMap.put("deleteRow", "Y");
		headMap.put("homeMmsMrn", "urn:mrn:smart-navi:device:mms1");
		headMap.put("certificate", "308006092A864886F70D010702A0803080020101310F300D06096086480165030402010500308006092A864886F70D010701A08024800409000102030405060708000000000000A0803082051E308204A5A00302010202143485B82A9D744B91DC19FE8A75498A6F9BFDB693300A06082A8648CE3D0403023081E931323030060A0992268993F22C6401010C2275726E3A6D726E3A6D636C3A63613A6D61726974696D65636C6F75642D6964726567310B300906035504061302444B3110300E06035504080C0744656E6D61726B3113301106035504070C0A436F70656E686167656E31163014060355040A0C0D4D61726974696D65436C6F756431163014060355040B0C0D4D61726974696D65436C6F75643128302606035504030C1F4D61726974696D65436C6F7564204964656E746974792052656769737472793125302306092A864886F70D0109011616696E666F406D61726974696D65636C6F75642E6E6574301E170D3137313032363037343433385A170D3235303130313030303030305A30818A310B300906035504061302444B311C301A060355040A0C1375726E3A6D726E3A6D636C3A6F72673A646D61310F300D060355040B0C0676657373656C3116301406035504030C0D504F554C204C4F57454E4F524E31343032060A0992268993F22C6401010C2475726E3A6D726E3A6D636C3A76657373656C3A646D613A706F756C2D6C6F77656E6F726E3076301006072A8648CE3D020106052B8104002203620004C199177E194B6E27518FDB7C04FA2083C6CED3784CCC58C41C1E5BFA6F834BAA104E7D42C07F16082B39E057AB30BB53EB7480995F575D46CA0D986AFB3583A90BC1B3D937C1B2D706A584909D099D79F5BC6B5329E5553E6EBE5C786F582E83A3820269308202653082013B0603551D11048201323082012EA02406146983ADE2EFF7DBB992B6C9A28DDF8E90BBFFEE4BA00C0C0A434F50454E484147454EA01F06146981A2A4C4C89BB080C7CBAAC8C080AAAED78A1BA0070C054F54484552A02106146983B6A397D89BAFF8C7CB99EC8080AAAED78A1BA0090C0739323530393639A02306146983EE9684809BAFF8C7CB8BA9C080AAAED78A1BA00B0C09323139393937303030A01A0614698286BBBBC89BB0A8C7CB9ED98080AAAED78A1BA0020C00A03E0614698398BCD7C09EF0F0C7CBAA9D8080AAAED78A1BA0260C2475726E3A6D726E3A6D636C3A76657373656C3A646D613A706F756C2D6C6F77656E6F726EA02106146983E692F5C89A98F8C7CBAEABC080AAAED78A1BA0090C0744454E4D41524BA01E06146982B988F0C09BAFF8C7CBA9BDC080AAAED78A1BA0060C044F5A5A58301F0603551D2304183016801436CAED056224D5E30251A9F3FDE14D4986B54CB0301D0603551D0E041604143B1A8E6F432F0B2145392E2593B57E65935E1B35306B0603551D1F046430623060A05EA05C865A68747470733A2F2F6170692E6D61726974696D65636C6F75642E6E65742F783530392F6170692F6365727469666963617465732F63726C2F75726E3A6D726E3A6D636C3A63613A6D61726974696D65636C6F75642D6964726567307706082B06010505070101046B3069306706082B06010505073001865B68747470733A2F2F6170692E6D61726974696D65636C6F75642E6E65742F783530392F6170692F6365727469666963617465732F6F6373702F75726E3A6D726E3A6D636C3A63613A6D61726974696D65636C6F75642D6964726567300A06082A8648CE3D040302036700306402300C2F1304CDD9CC4FED17ACCB05C6D8E5EFC92AE574F6AF6A2FA99D009743C2ADE4AE6B924144C7798AF2E3D0D806600F0230018332AB932ECF85F9A820868D68ECA86BEBF761D90C1FCB2D88C89DAFACED3CF18F6638B089FF4C008A160BFA84AE9700003182022930820225020101308201023081E931323030060A0992268993F22C6401010C2275726E3A6D726E3A6D636C3A63613A6D61726974696D65636C6F75642D6964726567310B300906035504061302444B3110300E06035504080C0744656E6D61726B3113301106035504070C0A436F70656E686167656E31163014060355040A0C0D4D61726974696D65436C6F756431163014060355040B0C0D4D61726974696D65436C6F75643128302606035504030C1F4D61726974696D65436C6F7564204964656E746974792052656769737472793125302306092A864886F70D0109011616696E666F406D61726974696D65636C6F75642E6E657402143485B82A9D744B91DC19FE8A75498A6F9BFDB693300D06096086480165030402010500A08195301806092A864886F70D010903310B06092A864886F70D010701301C06092A864886F70D010905310F170D3139303933303039313534385A302A06092A864886F70D010934311D301B300D06096086480165030402010500A10A06082A8648CE3D040302302F06092A864886F70D01090431220420F8348E0B1DF00833CBBBD08F07ABDECC10C0EFB78829D7828C62A7F36D0CC549300A06082A8648CE3D04030204673065023100ABB705FBD87D67735C0C2CBF33A7A5B60DBED30796CDC032FAE4A6882D3CB9CD712F9D7D03031F85FC8030ED7371EADB02302B0A4D6B0AF95C1EC0BDC7B1FA1550CDE5702F9FA944A26E095D97EF459D82FFE835837059791716DB67A51388E3705B000000000000");
		bodyMap.put("reqUri", "/entity/urn:mrn:imo:imo-no:1000003/home-mms");
		bodyMap.put("delMrn", "urn:mrn:imo:imo-no:1000003");
		//SC ip?
		bodyMap.put("ip", "");
		//SC port?
		bodyMap.put("port", "");

		org.json.simple.JSONObject jObjBody = null;
		org.json.simple.JSONObject jObjHead = null;
		try {
			jObjBody = (org.json.simple.JSONObject) new JSONParser().parse(org.json.simple.JSONObject.toJSONString(bodyMap));
			jObjHead = (org.json.simple.JSONObject) new JSONParser().parse(org.json.simple.JSONObject.toJSONString(headMap));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		String httpMethod = ",\"method\":\"DELETE\"";
		String req = "{\"callHomeManager\":{\"headerMap\":"+jObjHead.toJSONString()+", \"bodyMap\":"+jObjBody.toJSONString()+httpMethod+"}}";
		String queryReply = null;
		
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap bootStrap = new Bootstrap();
			bootStrap.group(group).channel(NioSocketChannel.class)
					.remoteAddress(new InetSocketAddress("localhost", 8588))
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(
									new MnsClientHandler(req)
								);
						}
					});
			ChannelFuture chnFuture = null;
			try {
				chnFuture = bootStrap.connect().sync();
				chnFuture.channel().closeFuture().sync();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} finally {
			try {
				group.shutdownGracefully().sync();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		int siz = MessageBlockingQue.getQueue().size();
		StringBuffer sb = new StringBuffer("");
		
		for(int i=0; i<siz; i++ ) {
			sb.append(MessageBlockingQue.getQueue().poll());
		}
		queryReply = sb.toString();
		logger.debug("\r\r\rpolllllllllll{}\r\r", sb.toString());
		logger.debug("\r\rreply result================={}\r",queryReply);
		
		org.json.simple.JSONObject postResultObject = null;
		org.json.simple.JSONArray jsonArray = null;
		try {
			
			jsonArray = (org.json.simple.JSONArray) new JSONParser().parse(queryReply);
			postResultObject = (org.json.simple.JSONObject) new JSONParser().parse(jsonArray.get(0).toString());
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Object statObj = postResultObject.get("httpStatus");
		int stat = -1;
		if(statObj instanceof Long) {
			stat = Integer.parseInt(Long.toString((long)postResultObject.get("httpStatus")));
			logger.debug("long {}",stat);
		}else if(statObj instanceof Integer) {
			stat = (int) postResultObject.get("httpStatus");
			logger.debug("int {}",stat);
		}
	}
	
}
