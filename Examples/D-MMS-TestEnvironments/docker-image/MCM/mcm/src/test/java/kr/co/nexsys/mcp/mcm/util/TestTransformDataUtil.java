package kr.co.nexsys.mcp.mcm.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

public class TestTransformDataUtil {

    public static ByteBuf stringToByteBuf(final String message) {
        return Unpooled.wrappedBuffer(message.getBytes());
    }

    public static String byteBufToString(final ByteBuf byteBuf) {
        return byteBuf.toString(Charset.defaultCharset());
    }
}
