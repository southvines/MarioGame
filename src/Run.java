import com.itheima.ui.GameFrame;
import com.itheima.util.KeyListener;
/**
  �������������ࡣ
 */
public class Run {
	//���������������
	public static void main(String[] args) throws Exception {
		GameFrame gf = new GameFrame();
		// ��������������
		KeyListener kl = new KeyListener(gf);
		// ��������Ӽ��̼�����
		gf.addKeyListener(kl);
	}
}
