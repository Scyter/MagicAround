package com.magicaround.lines;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/*Класс объекта игры. Т.е. единица изображения, которая будет появляться на экране. 
 В данном случае речь идет о прямоугольном объекте. 
 Кнопки, примеры и прочие виды текста, другие картинки будут экземплярами этого класса (или дочернего для него).

 Vector2 position - позиция обекта по осям Х и Y (речь идет о центре объекта)
 Rectangle bounds - границы объекта, задана нижняя точка и значения длинны и ширины
 */
public class GameObject {
	public final Vector2 position;
	public final Rectangle bounds;

	/*
	 * Конструктор класса. Рассчитывает также границы объекта исходя из центра,
	 * ширины и высоты float x - координата центра объекта по оси Х float y -
	 * координата центра объекта по оси Y float width, float height - ширина и
	 * высота объекта
	 */
	public GameObject(float x, float y, float width, float height) {
		this.position = new Vector2(x, y);
		this.bounds = new Rectangle(x - width / 2, y - height / 2, width,
				height);
	}
}
