package org.panda.bamboo.common.util.jackson.builder;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * 复合LocalTime反序列化器
 */
public class CompositeLocalTimeDeserializer extends LocalTimeDeserializer {

    private static final long serialVersionUID = -602236135563959125L;

    private LocalTimeDeserializer[] deserializers;

    public CompositeLocalTimeDeserializer(DateTimeFormatter formatter, DateTimeFormatter... moreFormatters) {
        super(formatter);
        this.deserializers = new LocalTimeDeserializer[moreFormatters.length];
        for (int i = 0; i < moreFormatters.length; i++) {
            this.deserializers[i] = new LocalTimeDeserializer(moreFormatters[i]);
        }
    }

    @Override
    public LocalTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        LocalTime time = null;
        try {
            time = super.deserialize(parser, context);
        } catch (Exception e) {
            // 没有更多反序列化器则抛出异常，否则忽略异常，交给后续反序列化器处理
            if (this.deserializers.length == 0) {
                throw e;
            }
        }
        if (time == null) {
            time = moreDeserialize(parser, context);
        }
        return time;
    }

    private LocalTime moreDeserialize(JsonParser parser, DeserializationContext context) throws IOException {
        for (int i = 0; i < this.deserializers.length; i++) {
            LocalTimeDeserializer deserializer = this.deserializers[i];
            try {
                LocalTime time = deserializer.deserialize(parser, context);
                if (time != null) {
                    return time;
                }
            } catch (Exception e) {
                // 没有更多反序列化器则抛出异常，否则忽略异常，交给后续反序列化器处理
                if (i >= this.deserializers.length - 1) {
                    throw e;
                }
            }
        }
        return null;
    }

}
