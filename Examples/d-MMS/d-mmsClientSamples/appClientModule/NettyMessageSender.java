

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

class NettyMessageSender {

	private static final Logger logger = LoggerFactory.getLogger(NettyMessageSender.class);
	
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
		
		//ObjectMapper mapper = new ObjectMapper();
		//ObjectNode node = mapper.createObjectNode();
		Map<String, String> node = new HashMap<String, String>();
		
		String geoType = "";
		int mod = //Integer.parseInt(args[0]);
		0;///test;immediate
		switch(mod) {
		case 0:
			geoType = "circle";

			node.put("srcMRN", "SP");
			node.put("dstMRN", "*");
			node.put("lat", "33.5499499324243");
			node.put("long", "176.720328966827");
			node.put("radius", "400");
			break;
		case 1:
			geoType = "polygon";

			node.put("srcMRN", "SP");
			node.put("dstMRN", "*");
			node.put("lat", "[34.796251, 43.315407,  4.439433,7.548697]");
			node.put("long", "[121.410482,172.756282,167.188200,111.833566]");
			break;
		case 2:
			geoType = "unicasting";

			node.put("srcMRN", "SP");
			node.put("dstMRN", "SP");
			node.put("IPAddr", "172.10.10.117");
			break;
		default:
			break;
		}
		
		String JSON_STRING = node.toString();
		
		/*
		ObjectNode nodeM = mapper.createObjectNode();
		
		JSON_STRING = JSON_STRING.replaceAll("\"", "");
		
		nodeM.put("geocasting_"+geoType, JSON_STRING);
		
		JSON_STRING = nodeM.toString();
		JSON_STRING = JSON_STRING.replaceAll("\"", "");
		*/
		
		JSONObject object = null;
		try {
			object = (JSONObject) new JSONParser().parse(JSONObject.toJSONString(node));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//Map<String, String> nodeM = new HashMap<String, String>();
		JSON_STRING = "{\"geocasting_"+geoType+"\":"+ object.toJSONString()+"}";

		//nodeM.put("geocasting_"+geoType, JSON_STRING);
		
		/*
		try {
			object = (JSONObject) new JSONParser().parse(JSONObject.toJSONString(nodeM));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		*/
		String req = JSON_STRING;
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
	}
	
}
