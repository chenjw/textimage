package com.chenjw.textimage.service.graphic.java2d;

import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.chenjw.textimage.service.config.StyleConfig;
import com.chenjw.textimage.service.config.StyleConfigHelper;
import com.chenjw.textimage.service.config.TextStyle;
import com.chenjw.textimage.service.constants.TextImageConstants;
import com.chenjw.textimage.service.exception.TextImageException;
import com.chenjw.textimage.service.model.TextLine;
import com.chenjw.textimage.service.spi.TextCanvas;

public class Java2dCanvas implements TextCanvas {

	private BufferedImage image;
	private Graphics2D g;

	Java2dCanvas(StyleConfig config, int width, int height) {
		if (TextImageConstants.IS_USE_TRANSLUCENT) {
			image = GraphicsEnvironment
					.getLocalGraphicsEnvironment()
					.getDefaultScreenDevice()
					.getDefaultConfiguration()
					.createCompatibleImage(width, height,
							Transparency.TRANSLUCENT);
		} else {
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		}

		g = (Graphics2D) image.getGraphics();
	}

	@Override
	public void buildImage(OutputStream os) throws TextImageException {
		try {
			g.dispose();
			ImageIO.write(image, TextImageConstants.IMAGE_FORMAT, os);
		} catch (IOException e) {
			throw new TextImageException("build image failed!", e);
		}

	}

	void drawTextLine(String text, TextStyle style, TextLine line) {
		if (style.getBackgroundColor() != null) {
			g.setBackground(style.getBackgroundColor());
			g.clearRect(line.getX(), line.getY(), line.getWidth(),
					line.getHeight());
		}
		TextLayout textLayout = new TextLayout(text,
				StyleConfigHelper.getFont(style),
				PaintEnvironment.getDefaultFontRenderContext());
		g.setColor(style.getColor());
		// 偏移
		double pX = -textLayout.getPixelBounds(null, 0, 0).getX();
		double pY = // (style.getLineHeight() - style.getFontSize()) / 2
		-textLayout.getPixelBounds(null, 0, 0).getY();
		if (style.getIsAntialias()) {
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		} else {
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_OFF);
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		}
		textLayout.draw(g, (int) Math.ceil((line.getX()
				+ line.getPadding().getLeft() + pX)), (int) Math.ceil((line
				.getY() + line.getPadding().getTop() + pY)));

	}
}