package com.github.forinil.hateoasduallayer.converter;

import com.github.forinil.hateoasduallayer.model.Right;
import org.dozer.DozerConverter;

public class RightModelToRightEntityConverter extends DozerConverter<Right, com.github.forinil.hateoasduallayer.entity.Right> {

    public RightModelToRightEntityConverter() {
        super(Right.class, com.github.forinil.hateoasduallayer.entity.Right.class);
    }

    @Override
    public com.github.forinil.hateoasduallayer.entity.Right convertTo(Right source, com.github.forinil.hateoasduallayer.entity.Right destination) {
        return com.github.forinil.hateoasduallayer.entity.Right.valueOf(source.name().substring(2));
    }

    @Override
    public Right convertFrom(com.github.forinil.hateoasduallayer.entity.Right source, Right destination) {
        return Right.valueOf("R_" + source.name());
    }
}
