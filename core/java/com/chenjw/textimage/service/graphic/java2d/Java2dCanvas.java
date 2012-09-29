package com.chenjw.textimage.service.graphic.java2d;

import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

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

	public Java2dCanvas(StyleConfig config, int width, int height) {
		// 计算画布尺寸
		int h;
		int w;
		if (config.getIsFixSize()) {
			w = config.getCanvasWidth();
			h = config.getCanvasHeight();
		} else {
			w = width;
			h = height;
		}
		boolean isUseTranslucent = false;
		if (config.getBackgroundColor() == null
				&& config.getBackgroundImage() == null) {
			isUseTranslucent = true;
		}
		// 创建image
		/**
		 * 背景样式显示规则
		 * <ul>
		 * <li>1. 优先使用backgroundImage的值</li>
		 * <li>2. 如果backgroundImage为null使用backgroundColor的值</li>
		 * <li>3. 如果backgroundColor为null使用透明背景</li>
		 * </ul>
		 * **/
		// 是否透明
		if (isUseTranslucent) {
			image = GraphicsEnvironment.getLocalGraphicsEnvironment()
					.getDefaultScreenDevice().getDefaultConfiguration()
					.createCompatibleImage(w, h, Transparency.TRANSLUCENT);
		} else {
			image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		}

		g = (Graphics2D) image.getGraphics();
		// 绘制背景
		if (config.getBackgroundImage() != null) {
			ByteArrayInputStream bais = new ByteArrayInputStream(
					config.getBackgroundImage());

			BufferedImage img = null;
			try {
				img = ImageIO.read(bais);
			} catch (IOException e) {
			} finally {
				IOUtils.closeQuietly(bais);
			}
			g.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
		} else if (config.getBackgroundColor() != null) {
			g.setBackground(config.getBackgroundColor());
			g.clearRect(0, 0, w, h);
		}
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