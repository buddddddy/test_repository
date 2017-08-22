package wsdl;

import lombok.SneakyThrows;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.CharEncoding;
import org.jetbrains.annotations.NotNull;
import org.mdlp.wsdl.ObjectFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;

public class WsdlUtils {

    @SneakyThrows
    public static <T extends WsdlType> String marshal(T obj) {
        try (StringWriter writer = new StringWriter()) {
            JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
            Marshaller MARSHALLER = context.createMarshaller();
            MARSHALLER.marshal(obj, writer);
            return writer.toString();
        }
    }

    @NotNull
    @SuppressWarnings("unchecked")
    @SneakyThrows
    public static <T extends WsdlType> T unmarshal(@NotNull Class<T> type, Reader reader) {
        JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
        Unmarshaller UNMARSHALLER = context.createUnmarshaller();
        Object obj = UNMARSHALLER.unmarshal(reader);
        if (!type.isInstance(obj)) throw new IllegalStateException(type + " is not assignable from " + obj.getClass());
        return (T) obj;
    }

    @NotNull
    @SneakyThrows
    public static <T extends WsdlType> T unmarshal(@NotNull Class<T> type, @NotNull String content) {
        BOMInputStream bomIn = new BOMInputStream(new ByteArrayInputStream(content.getBytes(CharEncoding.UTF_8)));
        try (Reader reader = new InputStreamReader(bomIn)) {
            return unmarshal(type, reader);
        }
    }
}
