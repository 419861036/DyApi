package kkd.common.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

import kkd.common.entity.ValidateImage;

public class ImageGenerater {
	public static char[] codeSequence = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
			'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
			'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9' };
	private Random random = new Random();
	/*
	 * 
	 */
	private void setBaseThime(Graphics g, ValidateImage vImage) {
		//填充颜色
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, vImage.getWidth(), vImage.getHeight());
		g.setFont(new Font("Fixedsys",Font.PLAIN, vImage
				.getContentFontSize()));
		
	}

	/**
	 * 
	 * @param 图像
	 * @param 输出流
	 *            图像
	 * @throws IOException
	 */
	private void outPut(BufferedImage bImage, OutputStream os)
			throws IOException {
		ImageIO.write(bImage, "jpeg", os);
	}

	private void disturbThime(Graphics g, ValidateImage vImage) {
		g.setColor(Color.cyan);
		// 随机产生干扰线
		for (int i = 0; i < 50; i++) {
			int x = random.nextInt(vImage.getWidth());
			x=x==0?1:x;
			int y = random.nextInt(vImage.getHeight());
			y=y==0?1:y;
			int x1 = random.nextInt(13);
			int y1 = random.nextInt(15);
			g.drawLine(x, y, x + x1, y + y1);
		}
	}

	/**
	 * 
	 * @throws IOException
	 */
	public void generater(ValidateImage vImage, OutputStream os)
			throws IOException {
		if(vImage.getContents()==null){
			vImage.setContents(this.getRandomString());
		}
		BufferedImage bImage = new BufferedImage(vImage.getWidth(),
				vImage.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bImage.createGraphics();
		setBaseThime(g, vImage);
		disturbThime(g, vImage);
		drawContents(g,vImage.getContents(),bImage);
		// 画边框
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, vImage.getWidth() - 1, vImage.getHeight() - 1);
		g.dispose();
		outPut(bImage, os);
	}
	
	private void drawContents(Graphics2D g,char content[],BufferedImage image){
		if(content == null || content.length == 0){
			return;
		}
		
		Font font = new Font("Fixedsys", Font.BOLD, 20);
		for (int i=0;i<content.length;i++) {
			Graphics2D g2 = image.createGraphics();
			g2.setFont(font);
			g2.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
			int angle=random.nextInt(60);
			g2.rotate(angle*Math.PI/ 180,13*(i+1),15);
			g2.translate(random.nextInt(3), random.nextInt(3));
			g2.drawString(String.valueOf(content[i]), 13*(i+1), 15);
			g2.dispose();
		}
	}
	public char[] getRandomString(){
		//生成验证码
		char[] validateCode = new char[5];
		for(int i=0;i<5;i++){
			int index = random.nextInt(codeSequence.length);
			validateCode[i] = codeSequence[index];
		}
		return validateCode;
	} 
}
