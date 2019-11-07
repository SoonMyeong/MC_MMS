package kr.co.nexsys.mcp.mcm.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import kr.co.nexsys.mcp.mcm.server.handler.McmServerHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class McmServer  {

	private final ServerProperties serverProperties;
	private final McmServerHandler mcmServerHandler;

  public void start() {
		EventLoopGroup bossGroup = new NioEventLoopGroup(serverProperties.getBossThreadCount());
		EventLoopGroup workerGroup = new NioEventLoopGroup(serverProperties.getWorkerThreadCount());

		try {
			ServerBootstrap serverBootStrap = new ServerBootstrap();
			serverBootStrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.TRACE)).childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch)
						 throws Exception
						{
							ChannelPipeline pipeline = ch.pipeline();
							ch.config().setRecvByteBufAllocator(new FixedRecvByteBufAllocator(64*2048));
							ch.config().setReceiveBufferSize(64*2048);
							pipeline.addLast(mcmServerHandler);
						}
					});
			int rcvBuf = 64;
			int sndBuf = 64;
			int lowWaterMark = 32;
			int highWaterMark = 64;
			serverBootStrap.option(ChannelOption.SO_RCVBUF, rcvBuf * 2048);
			serverBootStrap.option(ChannelOption.SO_SNDBUF, sndBuf * 2048);
			serverBootStrap.option(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(lowWaterMark * 2048, highWaterMark * 2048));

			ChannelFuture channelFuture = serverBootStrap.bind(serverProperties.getPort()).sync();
			channelFuture.channel().closeFuture().sync();

		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}