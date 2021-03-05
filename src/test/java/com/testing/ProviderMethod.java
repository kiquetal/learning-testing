package com.testing;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public class ProviderMethod {


    static Stream<Arguments> sourceStream_StringDouble() {

        return Stream.of(arguments("apple",8.0),arguments("pear",4.5));

    }


}
