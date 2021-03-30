package com.github.un1imited41.internship.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.un1imited41.internship.entities.Variable;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.List;

public class VariableDeserializer extends JsonDeserializer<List<Variable>> {

    @Override
    public List<Variable> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        List<Variable> list = Lists.newArrayList();
        ArrayNode arrayNode = jsonParser.readValueAs(ArrayNode.class);
        arrayNode.forEach(node -> node.fieldNames().forEachRemaining(name -> list
                .add(new Variable(name, node.get(name).asText()))));
        return list;
    }
}
