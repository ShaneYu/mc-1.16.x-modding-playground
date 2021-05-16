package com.github.shaneyu.playground.common.item.interfaces;

import com.github.shaneyu.playground.common.util.NamedColor;
import com.github.shaneyu.playground.lib.enums.IIncrementalEnum;
import com.github.shaneyu.playground.lib.text.IHasTextComponent;

public interface IRadialSelectorEnum<TYPE extends Enum<TYPE> & IRadialSelectorEnum<TYPE>> extends IIncrementalEnum<TYPE>, IHasTextComponent {
    default NamedColor getColor() {
        return null;
    }
}
