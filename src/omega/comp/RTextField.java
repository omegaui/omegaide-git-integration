package omega.comp;
import java.awt.RenderingHints;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.JTextField;
public class RTextField extends JTextField{
	//R means Round
     private String hint;
     private String pressHint;
	private Shape shape;
	private int arcX = 10;
	private int arcY = 10;
	private Color color1;
	private Color color2;
	private Color color3;
	public RTextField(String hint, String pressHint, Color color1, Color color2, Color color3){
		this.hint = hint;
          this.pressHint = pressHint;
		setColors(color1, color2, color3);
          setText(hint);
          setForeground(color3);
          setCaretColor(color1);
		setOpaque(false);
		addMouseListener(new MouseAdapter(){
			@Override
			public void mouseExited(MouseEvent e){
				if(getText().equals("") || getText().equals(RTextField.this.pressHint)){
					setText(RTextField.this.hint);
                         repaint();
				}
			}
			@Override
			public void mousePressed(MouseEvent e){
				if(getText().equals(RTextField.this.hint)){
					setText(RTextField.this.pressHint);
                         repaint();
				}
			}
		});
	}
     public void setHint(String hint) {
          this.hint = hint;
     }
     public void setPressHint(String pressHint) {
          this.pressHint = pressHint;
     }
	public void setArc(int x, int y){
		this.arcX = x;
		this.arcY = y;
	}
	public void setColors(Color c1, Color c2, Color c3){
		this.color1 = c1;
		this.color2 = c2;
		this.color3 = c3;
		setBackground(color2);
		setForeground(color3);
	}
	protected void paintComponent(Graphics g) {
		g.setColor(getBackground());
		g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcX, arcY);
          if(!getText().equals(hint))
		     super.paintComponent(g);
          else{
               Graphics2D g2d = (Graphics2D)g;
               g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
               g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
               g2d.setColor(getForeground());
               g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcX, arcY);
               g2d.drawString(hint, getWidth()/2 - g2d.getFontMetrics().stringWidth(hint)/2, 
                    getHeight()/2 - g2d.getFontMetrics().getHeight()/2 + g2d.getFontMetrics().getAscent() - g2d.getFontMetrics().getDescent() + 1);
               g2d.dispose();
          }
	}
     public boolean hasText(){
     	return !(getText().equals(hint) || getText().equals(pressHint));
     }
	protected void paintBorder(Graphics g) {
		g.setColor(getForeground());
		g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcX, arcY);
	}
	public boolean contains(int x, int y) {
		if (shape == null || !shape.getBounds().equals(getBounds())) {
			shape = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, arcX, arcY);
		}
		return shape.contains(x, y);
	}
}
