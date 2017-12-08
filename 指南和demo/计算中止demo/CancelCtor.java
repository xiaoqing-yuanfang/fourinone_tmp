import com.fourinone.Contractor;
import com.fourinone.WareHouse;
import com.fourinone.WorkerLocal;
import java.util.ArrayList;

public class CancelCtor extends Contractor
{
	public WareHouse giveTask(WareHouse inhouse)
	{
		WorkerLocal[] wks = getWaitingWorkers("cancelworker");
		System.out.println("wks.length:"+wks.length);
		
		WareHouse[] hmarr = new WareHouse[wks.length];
		for(int i=0;i<wks.length;i++){
			hmarr[i] = wks[i].doTask(new WareHouse());//3
		}
		
		for(int j=0;j<hmarr.length;){//��¼��ɵĽ����
			for(int i=0;i<wks.length;i++){//������Ƿ����
				if(hmarr[i]!=null&&hmarr[i].getStatus()==WareHouse.READY){
					System.out.println(i+":"+hmarr[i]);
					//�ҵ�888��,ֹͣ�������˼���
					if((Integer)hmarr[i].getObj("result")==888){
						for(int k=0;k<wks.length;k++){
							if(k!=i)
								wks[k].interrupt();
						}
					}
					hmarr[i]=null;
					j++;
				}
			}
		}
		
		/*try{Thread.sleep(5000L);}catch(Exception ex){}*/
		
		return null;
	}
	
	public static void main(String[] args)
	{
		CancelCtor a = new CancelCtor();
		a.giveTask(null);
		a.exit();
	}
}