package jf.chromatter;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import jf.chromatter.chat.FormattedMessage;
import jf.chromatter.chat.format.ChatFormatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ChromatterBukkitTest {
    private final PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();
    private ChromatterBukkit plugin;
    private PlayerMock player;

    @BeforeEach
    void setUp() {
        ServerMock server = MockBukkit.mock();
        this.plugin = MockBukkit.load(ChromatterBukkit.class);
        this.player = server.addPlayer();
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void gradientPriorityTest() {

        final String message = "&#ff0000->#ffffff->#0000ff&l&hover[&mHover]&cmd[/test]Testing red->white->blue gradient.";
        final List<Component> components = this.formatMessage(message);
        assertEquals(34, components.size());
        String previousHex = "";
        for (final Component component : components) {

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
        assertEquals("#32cd32", first.color().asHexString());
        assertNull(second.color());
    }

    @Test
    @SuppressWarnings({"deprecation", "ConstantConditions"})
    public void replacementTest() {
        final String message = "&lThis message contains a &a{placeholder}";
        final FormattedMessage formattedMessage = new FormattedMessage(message, ChatFormatter.defaultFormatters(true));
        this.player.spigot().sendMessage(formattedMessage.getTextComponents(new String[]{"{placeholder}"}, new String[]{"&r lol an unformatted reset"}));
        final Component first = this.player.nextComponentMessage();
        final Component second = this.player.nextComponentMessage();

        assertTrue(first.hasDecoration(TextDecoration.BOLD));
        assertEquals("This message contains a ", this.serializer.serialize(first));

        assertNotNull(second.color());
        assertEquals("#55ff55", second.color().asHexString());
        assertEquals("&r lol an unformatted reset", this.serializer.serialize(second));

    }

    @Test
    public void platformCheckTest() {
        assertEquals(ServerPlatform.getPlatformName(), ServerPlatform.PlatformName.PAPER);
    }

    @SuppressWarnings("deprecation")
    private List<Component> formatMessage(final String message) {
        final FormattedMessage formattedMessage = new FormattedMessage(message, ChatFormatter.defaultFormatters(true));
        this.player.spigot().sendMessage(formattedMessage.getTextComponents());
        return Stream.generate(this.player::nextComponentMessage).takeWhile(Objects::nonNull).toList();
    }

}