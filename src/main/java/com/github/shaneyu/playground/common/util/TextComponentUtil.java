package com.github.shaneyu.playground.common.util;

import com.github.shaneyu.playground.common.PlaygroundLang;
import com.github.shaneyu.playground.lib.localization.IHasTranslationKey;
import com.github.shaneyu.playground.lib.text.IHasTextComponent;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public final class TextComponentUtil {
    private TextComponentUtil() {}

    public static IFormattableTextComponent build(Object... components) {
        IFormattableTextComponent result = null;
        Style cachedStyle = Style.EMPTY;

        for (Object component : components) {
            if (component == null) {
                // If the component doesn't exist just skip it
                continue;
            }

            IFormattableTextComponent current = null;

            if (component instanceof IHasTextComponent) {
                current = ((IHasTextComponent) component).getTextComponent().deepCopy();
            } else if (component instanceof IHasTranslationKey) {
                current = translate(((IHasTranslationKey) component).getTranslationKey());
            } else if (component instanceof NamedColor) {
                cachedStyle = cachedStyle.setColor(((NamedColor) component).getColor());
            } else if (component instanceof ITextComponent) {
                // Just append if a text component is being passed
                current = ((ITextComponent) component).deepCopy();
            } else if (component instanceof TextFormatting) {
                cachedStyle = cachedStyle.applyFormatting((TextFormatting) component);
            } else if (component instanceof ClickEvent) {
                cachedStyle = cachedStyle.setClickEvent((ClickEvent) component);
            } else if (component instanceof HoverEvent) {
                cachedStyle = cachedStyle.setHoverEvent((HoverEvent) component);
            } else if (component instanceof Block) {
                current = translate(((Block) component).getTranslationKey());
            } else if (component instanceof Item) {
                current = translate(((Item) component).getTranslationKey());
            } else if (component instanceof ItemStack) {
                current = ((ItemStack) component).getDisplayName().deepCopy();
            } else if (component instanceof FluidStack) {
                current = translate(((FluidStack) component).getTranslationKey());
            } else if (component instanceof Fluid) {
                current = translate(((Fluid) component).getAttributes().getTranslationKey());
            } else if (component instanceof Direction) {
                current = getTranslatedDirection((Direction) component);
            } else if (component instanceof String || component instanceof Boolean || component instanceof Number) {
                // Put actual boolean or integer/double, etc value
                current = getString(component.toString());
            } else {
                // Fallback to a generic replacement
                current = PlaygroundLang.GENERIC.translate(component);
            }
            if (current == null) {
                // If we don't have a component to add, don't
                continue;
            }
            if (!cachedStyle.isEmpty()) {
                // Apply the style and reset
                current.setStyle(cachedStyle);
                cachedStyle = Style.EMPTY;
            }
            if (result == null) {
                result = current;
            } else {
                result.appendSibling(current);
            }
        }

        // Ignores any trailing formatting
        return result;
    }

    public static TranslationTextComponent smartTranslate(String key, Object... components) {
        if (components.length == 0) {
            // If we don't have any args just short circuit to creating the translation key
            return translate(key);
        }

        List<Object> args = new ArrayList<>();
        Style cachedStyle = Style.EMPTY;

        for (Object component : components) {
            if (component == null) {
                // If the component doesn't exist add it anyways, because we may want to be replacing it
                // with a literal null in the formatted text
                args.add(null);
                cachedStyle = Style.EMPTY;

                continue;
            }

            IFormattableTextComponent current = null;

            if (component instanceof IHasTextComponent) {
                current = ((IHasTextComponent) component).getTextComponent().deepCopy();
            } else if (component instanceof IHasTranslationKey) {
                current = translate(((IHasTranslationKey) component).getTranslationKey());
            } else if (component instanceof Block) {
                current = translate(((Block) component).getTranslationKey());
            } else if (component instanceof Item) {
                current = translate(((Item) component).getTranslationKey());
            } else if (component instanceof ItemStack) {
                current = ((ItemStack) component).getDisplayName().deepCopy();
            } else if (component instanceof FluidStack) {
                current = translate(((FluidStack) component).getTranslationKey());
            } else if (component instanceof Fluid) {
                current = translate(((Fluid) component).getAttributes().getTranslationKey());
            } else if (component instanceof Direction) {
                current = getTranslatedDirection((Direction) component);
            }

            // Formatting
            else if (component instanceof NamedColor && cachedStyle.getColor() == null) {
                // No color set yet in the cached style, apply the color
                cachedStyle = cachedStyle.setColor(((NamedColor) component).getColor());
                continue;
            } else if (component instanceof TextFormatting && !hasStyleType(cachedStyle, (TextFormatting) component)) {
                // Specific formatting not in the cached style yet, apply it
                cachedStyle = cachedStyle.applyFormatting((TextFormatting) component);
                continue;
            } else if (component instanceof ClickEvent && cachedStyle.getClickEvent() == null) {
                // No click event set yet in the cached style, add the event
                cachedStyle = cachedStyle.setClickEvent((ClickEvent) component);
                continue;
            } else if (component instanceof HoverEvent && cachedStyle.getHoverEvent() == null) {
                // No hover event set yet in the cached style, add the event
                cachedStyle = cachedStyle.setHoverEvent((HoverEvent) component);
                continue;
            } else if (!cachedStyle.isEmpty()) {
                // Only bother attempting these checks if we have a cached format, because
                // otherwise we are just going to want to use the raw text
                if (component instanceof ITextComponent) {
                    // Just append if a text component is being passed
                    current = ((ITextComponent) component).deepCopy();
                } else if (component instanceof String || component instanceof Boolean || component instanceof Number) {
                    // Put actual boolean or integer/double, etc value
                    current = getString(component.toString());
                } else if (component instanceof NamedColor) {
                    // If we already have a color in our format allow using the EnumColor's name
                    current = ((NamedColor) component).getName();
                } else {
                    // Fallback to a direct replacement just so that we can properly color it
                    current = PlaygroundLang.GENERIC.translate(component);
                }
            } else if (component instanceof String) {
                // If we didn't format it and it is a string make sure we clean it up
                component = cleanString((String) component);
            }

            if (!cachedStyle.isEmpty()) {
                // If we don't have a text component, then we have to just ignore the formatting and
                // add it directly as an argument. (Note: This should never happen because of the fallback)
                if (current == null) {
                    args.add(component);
                } else {
                    // Otherwise we apply the formatting and then add it
                    args.add(current.setStyle(cachedStyle));
                }

                cachedStyle = Style.EMPTY;
            } else if (current == null) {
                // Add raw
                args.add(component);
            } else {
                // Add the text component variant of it
                args.add(current);
            }
        }

        if (!cachedStyle.isEmpty()) {
            // Add trailing formatting as a color name or just directly
            // Note: We know that we have at least one element in the array, so we don't need to safety check here
            Object lastComponent = components[components.length - 1];

            if (lastComponent instanceof NamedColor) {
                args.add(((NamedColor) lastComponent).getName());
            } else {
                args.add(lastComponent);
            }
        }

        return translate(key, args.toArray());
    }

    private static IFormattableTextComponent getTranslatedDirection(Direction direction) {
        switch (direction) {
            case DOWN:
                return PlaygroundLang.DOWN.translate();
            case UP:
                return PlaygroundLang.UP.translate();
            case NORTH:
                return PlaygroundLang.NORTH.translate();
            case SOUTH:
                return PlaygroundLang.SOUTH.translate();
            case WEST:
                return PlaygroundLang.WEST.translate();
            case EAST:
                return PlaygroundLang.EAST.translate();
        }

        return getString(direction.toString());
    }

    public static StringTextComponent getString(String component) {
        return new StringTextComponent(cleanString(component));
    }

    private static String cleanString(String component) {
        return component.replace("\u00A0", " ");
    }

    public static TranslationTextComponent translate(String key, Object... args) {
        return new TranslationTextComponent(key, args);
    }

    private static boolean hasStyleType(Style current, TextFormatting formatting) {
        switch (formatting) {
            case OBFUSCATED:
                return current.getObfuscated();
            case BOLD:
                return current.getBold();
            case STRIKETHROUGH:
                return current.getStrikethrough();
            case UNDERLINE:
                return current.getUnderlined();
            case ITALIC:
                return current.getItalic();
            case RESET:
                return current.isEmpty();
            default:
                return current.getColor() != null;
        }
    }
}
