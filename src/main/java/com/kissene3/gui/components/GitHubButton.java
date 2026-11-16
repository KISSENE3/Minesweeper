package com.kissene3.gui.components;

import com.github.weisj.jsvg.SVGDocument;
import com.github.weisj.jsvg.parser.LoaderContext;
import com.github.weisj.jsvg.parser.SVGLoader;
import com.github.weisj.jsvg.view.ViewBox;
import com.kissene3.gui.models.Theme;
import com.kissene3.io.AppLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public abstract class GitHubButton extends MyAbstractButton {
	@SuppressWarnings("FieldCanBeLocal")
	private final String profileUrl = "https://github.com/KISSENE3/Minesweeper";
	@SuppressWarnings("FieldCanBeLocal")
	private final String resourcePath = "/icons/GitHub_Icon.svg";
	
	private String rawSvgTemplate;
	private SVGDocument iconImage;
	private Theme lastTheme;
	
	public GitHubButton(int x, int y, int size) {
		super(x, y, size, size);
		
		try (InputStream is = getClass().getResourceAsStream(this.resourcePath)) {
			if (is != null) {
				this.rawSvgTemplate = new String(is.readAllBytes(), StandardCharsets.UTF_8);
				this.updateIconIfNeeded();
			} else {
				AppLogger.info("Resource not found: " + resourcePath);
			}
		} catch (Exception e) {
			AppLogger.info("Could not load SVG from: " + resourcePath + " -> " + e.getMessage());
		}
	}
	
	private void updateIconIfNeeded() {
		if (this.rawSvgTemplate == null)
			return;
		
		Theme currentTheme = this.getTheme();
		if (Objects.equals(this.lastTheme, currentTheme))
			return;
		
		this.lastTheme = currentTheme;
		
		if (currentTheme == null || currentTheme.github_background() == null || currentTheme.github_foreground() == null) {
			return;
		}
		
		String bgColor = String.format("#%06X", (0xFFFFFF & currentTheme.github_background().getRGB()));
		String fgColor = String.format("#%06X", (0xFFFFFF & currentTheme.github_foreground().getRGB()));
		
		String coloredSvg = this.rawSvgTemplate
				.replace("%BG_COLOR%", bgColor)
				.replace("%FG_COLOR%", fgColor);
		
		try (ByteArrayInputStream bais = new ByteArrayInputStream(coloredSvg.getBytes(StandardCharsets.UTF_8))) {
			SVGLoader loader = new SVGLoader();
			this.iconImage = loader.load(bais, null, LoaderContext.createDefault());
		} catch (Exception e) {
			AppLogger.info("Could not parse SVG with updated theme -> " + e.getMessage());
		}
	}
	
	abstract public Theme getTheme();
	
	@Override
	public boolean contains(int x, int y) {
		int centerX = this.getX() + this.getWidth() / 2;
		int centerY = this.getY() + this.getHeight() / 2;
		int radius = this.getWidth() / 2;
		
		int dx = x - centerX;
		int dy = y - centerY;
		
		return (dx * dx + dy * dy) <= (radius * radius);
	}
	
	@Override
	public void onClick(@NotNull MouseEvent e) {
		if (e.getButton() != MouseEvent.BUTTON1)
			return;
		
		try {
			if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
				Desktop.getDesktop().browse(new URI(this.profileUrl));
			}
		} catch (Exception ex) {
			AppLogger.info("Could not open " + this.profileUrl + " -> " + ex.getMessage());
		}
	}
	
	@Override
	public void draw(@NotNull Graphics2D g) {
		this.updateIconIfNeeded();
		
		Graphics2D g2d = (Graphics2D) g.create();
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		
		int x = this.getX();
		int y = this.getY();
		int size = this.getWidth();
		
		if (this.isPressed) {
			double scale = 0.90;
			double centerX = x + size / 2.0;
			double centerY = y + size / 2.0;
			
			g2d.translate(centerX, centerY);
			g2d.scale(scale, scale);
			g2d.translate(-centerX, -centerY);
		}
		
		if (this.iconImage != null) {
			Graphics2D iconG = (Graphics2D) g2d.create();
			iconG.translate(x, y);
			this.iconImage.render(null, iconG, new ViewBox(0, 0, size, size));
			iconG.dispose();
		} else {
			g2d.setColor(this.getTheme().github_background());
			g2d.fillOval(x, y, size, size);
		}
		
		if (this.isPressed) {
			g2d.setColor(this.getTheme().overlay());
			g2d.fillOval(x, y, size, size);
			g2d.fillOval(x, y, size, size);
		} else if (this.isHovered) {
			g2d.setColor(this.getTheme().overlay());
			g2d.fillOval(x, y, size, size);
		}
		
		g2d.dispose();
	}
}