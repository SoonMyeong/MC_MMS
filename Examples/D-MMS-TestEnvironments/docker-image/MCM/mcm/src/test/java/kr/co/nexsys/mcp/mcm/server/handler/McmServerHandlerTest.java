package kr.co.nexsys.mcp.mcm.server.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import kr.co.nexsys.mcp.mcm.geocasting.dao.dvo.CoreTRDvo;
import kr.co.nexsys.mcp.mcm.geocasting.service.GeocastCircleService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static kr.co.nexsys.mcp.mcm.server.handler.McmServerHandler.errorJsonObject;
import static kr.co.nexsys.mcp.mcm.server.handler.McmServerHandler.resultPolling;
import static kr.co.nexsys.mcp.mcm.server.handler.McmServerHandlerTest.RequestType.UnicastingToSC;
import static kr.co.nexsys.mcp.mcm.server.handler.McmServerHandlerTest.RequestType.UnicastingToSP;
import static kr.co.nexsys.mcp.mcm.server.handler.McmServerHandlerTest.TestHelperUtil.buildCoreTRDvo;
import static kr.co.nexsys.mcp.mcm.server.handler.McmServerHandlerTest.TestHelperUtil.buildRequestMessge;
import static kr.co.nexsys.mcp.mcm.server.handler.McmServerHandlerTest.TestHelperUtil.buildResponse;
import static kr.co.nexsys.mcp.mcm.util.TestTransformDataUtil.byteBufToString;
import static kr.co.nexsys.mcp.mcm.util.TestTransformDataUtil.stringToByteBuf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Slf4j
public class McmServerHandlerTest {

    private EmbeddedChannel channel;

    @InjectMocks
    private McmServerHandler mcmServerHandler;

    @Mock
    private GeocastCircleService service;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        channel = new EmbeddedChannel(mcmServerHandler);
    }

    @After
    public void close() {
        if (channel != null) {
            channel.finishAndReleaseAll();
        }

    }

    @Test
    public void shouldWriteErrorMessageOfAbsentDstMrnWhenUnicasting() {
        final String message = buildRequestMessge(UnicastingToSC);
        final String expected = buildResponse(errorJsonObject(UnicastingToSC.getDstMrn()));
        log.debug("expected: {}", expected);

        when(service.findByMrn(anyString())).thenReturn(Lists.emptyList());

        channel.writeInbound(stringToByteBuf(message));

        assertThat(channel.outboundMessages().size(), equalTo(2));
        assertThat(byteBufToString(channel.readOutbound()), equalTo(expected));
        assertThat(channel.readOutbound(), equalTo(Unpooled.EMPTY_BUFFER));
    }

    @Test
    public void shouldWriteMessageByUnicastingToSC() {
        final CoreTRDvo dvo = buildCoreTRDvo("222.222.22.11", "9999", 1);
        final String message = buildRequestMessge(UnicastingToSC);
        final String expected = buildResponse(
        		///resultPolling(UnicastingToSC.getDstMrn(), dvo.getCommNo())
        		McmServerHandler.resultPush(UnicastingToSC.getDstMrn(), dvo.getCommNo())
        		);
        log.debug("message: {}", message);
        log.debug("expected: {}", expected);

        when(service.findByMrn(anyString()))
                .thenReturn(Lists.list(dvo));

        channel.writeInbound(stringToByteBuf(message));

        assertThat(channel.outboundMessages().size(), equalTo(2));
        assertThat(byteBufToString(channel.readOutbound()), equalTo(expected));
        assertThat(channel.readOutbound(), equalTo(Unpooled.EMPTY_BUFFER));
    }

	@Test
    public void shouldWriteMessageByUnicastingToSP() {
        final CoreTRDvo dvo = buildCoreTRDvo("222.222.22.11", "9999", 1);
        final String message = buildRequestMessge(UnicastingToSP);
        final String expected = buildResponse(
                McmServerHandler.resultPush(UnicastingToSP.getDstMrn(), dvo.getSIp(), dvo.getPortNumber()));

        log.debug("message: {}", message);
        log.debug("expected: {}", expected);

        when(service.findByMrn(anyString()))
                .thenReturn(Lists.list(dvo));

        channel.writeInbound(stringToByteBuf(message));

        assertThat(channel.outboundMessages().size(), equalTo(2));
        assertThat(byteBufToString(channel.readOutbound()), equalTo(expected));
        assertThat(channel.readOutbound(), equalTo(Unpooled.EMPTY_BUFFER));
    }


    enum RequestType {
        UnicastingToSC("urn:mrn:mcp:sc12"),
        UnicastingToSP("urn:mrn:mcp:sp12"),
        GeocastingCircle("*"),
        GeocastingPolygon("*");

        private final String dstMrn;

        RequestType(final String dstMrn) {
            this.dstMrn = dstMrn;
        }

        String getDstMrn() {
            return dstMrn;
        }
    }

    static class TestHelperUtil {

        static String buildRequestMessge(final RequestType requestType) {
            switch (requestType) {
                case UnicastingToSC:
                    return "{\"unicasting\":{\"srcMRN\":\"URN:MRN:MCP:VESSEL:SMART:IMO-9436094\",\"dstMRN\":\"" + UnicastingToSC.dstMrn + "\",\"IPAddr\":\"127.0.0.1\"}}";
                case UnicastingToSP:
                    return "{\"unicasting\":{\"srcMRN\":\"URN:MRN:MCP:VESSEL:SMART:IMO-9436094\",\"dstMRN\":\"" + UnicastingToSP.dstMrn + "\",\"IPAddr\":\"127.0.0.1\"}}";
                case GeocastingCircle:
                    return "";
                case GeocastingPolygon:
                    return "";
                default:
                    throw new IllegalArgumentException();
            }

        }

        static CoreTRDvo buildCoreTRDvo(final String ip, final String port, final int commNo) {
            return CoreTRDvo.builder().sIp(ip).portNumber(port).commNo(1).build();
        }

        static String buildResponse(final JSONObject jsonObject) {
            final JSONArray retJsonArray = new JSONArray();
            retJsonArray.add(jsonObject);
            return retJsonArray.toString();
        }

    }
}
