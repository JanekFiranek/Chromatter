package jf.chromatools;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import jf.chromatools.chat.ChatCode;
import jf.chromatools.chat.FormattedMessage;
import jf.chromatools.chat.format.ChatFormatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ChromaToolsBukkitTest {
    private ServerMock server;
    private ChromaToolsBukkit plugin;
    private PlayerMock player;
    private final PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();

    @BeforeEach
    void setUp() {
        this.server = MockBukkit.mock();
        this.plugin = MockBukkit.load(ChromaToolsBukkit.class);
        this.player = this.server.addPlayer();
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void gradientPriorityTest() {

        final String message = "&#ff0000->#ffffff&l&hover[&mHover]&cmd[/test]Testing red->white gradient.";
        final List<Component> components = this.formatMessage(message);
        assertEquals(28, components.size());
        String previousHex = "";
        for(final Component component : components) {

            assertTrue(component.hasDecoration(TextDecoration.BOLD));
            assertEquals(1, this.serializer.serialize(component).length());

            assertNotNull(component.clickEvent());
            assertEquals(ClickEvent.Action.RUN_COMMAND, component.clickEvent().action());
            assertEquals("/test", component.clickEvent().value());

            assertNotNull(component.hoverEvent());
            assertTrue(((Component) component.hoverEvent().value()).hasDecoration(TextDecoration.STRIKETHROUGH));
            assertEquals("Hover", this.serializer.serialize((Component) component.hoverEvent().value()));

            assertNotNull(component.color());
            assertNotEquals(previousHex, component.color().asHexString());
            previousHex = component.color().asHexString();
        }
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void configTest() {
        assertTrue(new File(this.plugin.getDataFolder(), "config.yml").exists());

        final String message = "&[lime]Everything up to this sign&r should be lime.";
        final List<Component> components = this.formatMessage(message);
        assertEquals(2, components.size());
        final Component first = components.get(0);
        final Component second = components.get(1);
        assertEquals("Everything up to this sign", this.serializer.serialize(first));
        assertEquals(" should be lime.", this.serializer.serialize(second));
        assertNotNull(first.color());
        assertEquals(0x32cd32, first.color().value());
        assertNull(second.color());
    }

    @Test
    @SuppressWarnings("deprecation")
    public void replacementTest() {
        final String message = "&lThis message contains a &a{placeholder}";
        final FormattedMessage formattedMessage = new FormattedMessage(message, ChatFormatter.defaultFormatters(true));
        this.player.spigot().sendMessage(formattedMessage.getTextComponents(new String[]{ "{placeholder}"}, new String[]{ "&r lol an unformatted reset"}));
        final Component first = this.player.nextComponentMessage();
        final Component second = this.player.nextComponentMessage();

        assertTrue(first.hasDecoration(TextDecoration.BOLD));
        assertEquals("This message contains a ", this.serializer.serialize(first));

        assertNotNull(second.color());
        assertEquals(0x55ff55, second.color().value());
        assertEquals("&r lol an unformatted reset", this.serializer.serialize(second));


    }

    @SuppressWarnings("deprecation")
    private List<Component> formatMessage(final String message) {
        final FormattedMessage formattedMessage = new FormattedMessage(message, ChatFormatter.defaultFormatters(true));
        this.player.spigot().sendMessage(formattedMessage.getTextComponents());
        return Stream.generate(this.player::nextComponentMessage).takeWhile(Objects::nonNull).toList();
    }
}