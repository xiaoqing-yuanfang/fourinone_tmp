import com.fourinone.BeanContext;
import com.fourinone.CoolHashClient;
import com.fourinone.CoolHashMap;
import com.fourinone.CoolHashMap.CoolKeySet;
import com.fourinone.CoolKeyResult;
import com.fourinone.CoolHashResult;
import com.fourinone.Filter.ValueFilter;
import com.fourinone.CoolHashException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Comparator;
import java.util.Arrays;

public class RunClient
{	
	public static void singleDemo(String[] args){
		CoolHashClient chc = BeanContext.getCoolHashClient(args[0],Integer.parseInt(args[1]));
		try{
			long start = System.currentTimeMillis();
			chc.put("name","zhang");//д���ַ�
			chc.put("age",20);//д������
			chc.put("weight",50.55f);//д�븡����
			chc.put("price",100.5588d);//д��double��
			chc.put("user.001.id",10000000l);//д�볤����
			chc.put("user.001.birthday",new Date());//д�����ڶ���
			chc.put("user.001.pet",new ArrayList());//д�뼯�϶���
			
			System.out.println((String)chc.get("name"));//��ȡ�ַ�
			System.out.println((int)chc.get("age"));//��ȡ����
			System.out.println((float)chc.get("weight"));//��ȡ������
			System.out.println((double)chc.get("price"));//��ȡdouble��
			System.out.println((long)chc.get("user.001.id"));//��ȡ������
			System.out.println((Date)chc.get("user.001.birthday"));//��ȡ���ڶ���
			System.out.println((ArrayList)chc.get("user.001.pet"));//��ȡ���϶���
			System.out.println("time taken in MS--"+(System.currentTimeMillis()-start));
		}catch(Exception ex){
			ex.printStackTrace();
		}
		chc.exit();
	}
	
	public static void batchDemo(String[] args){
		CoolHashClient chc = BeanContext.getCoolHashClient(args[0],Integer.parseInt(args[1]));
		try{
			long start = System.currentTimeMillis();
			CoolHashMap hm=new CoolHashMap();
			for(long i=0;i<1000000;i++){
				hm.put(i+"",i+"");//����1������k/v���ݵ�CoolHashMap����
			}
			System.out.println("load time taken in MS--"+(System.currentTimeMillis()-start));
			
			start = System.currentTimeMillis();
			int n=chc.put(hm);//����д��1������k/v����
			System.out.println("putBatch time taken in MS--"+(System.currentTimeMillis()-start));
			System.out.println("putBatch total:"+n);
			
			start = System.currentTimeMillis();
			CoolHashMap chm = chc.get(hm.getKeys());//������ȡ1������k/v����
			System.out.println("getBatch time taken in MS--"+(System.currentTimeMillis()-start));
			System.out.println("getBatch total:"+chm.size());
		}catch(Exception ex){
			ex.printStackTrace();
		}
		chc.exit();
	}
	
	public static void findDemo(String[] args){
		CoolHashClient chc = BeanContext.getCoolHashClient(args[0],Integer.parseInt(args[1]));
		try{
			long start = System.currentTimeMillis();
			CoolHashMap hm=new CoolHashMap();//CoolHashMapĬ���������Ϊ1���������ݣ��ɸ����ڴ��С����HASHCAPACITY������
			for(long i=0;i<100000;i++){
				hm.put("user."+i+".name",i+"name");
				hm.put("user."+i+".age",i);
			}
			System.out.println("load time taken in MS--"+(System.currentTimeMillis()-start));
			start = System.currentTimeMillis();
			int n=chc.put(hm);//����д����Ҫ��ѯ������
			System.out.println("put time taken in MS--"+(System.currentTimeMillis()-start));
			System.out.println("put total:"+n);
			
			start = System.currentTimeMillis();
			CoolHashResult hr100 = chc.find("user.100.*");//��ѯ�û�100����������
			CoolHashMap user100 = hr100.nextBatch(50);//ָ��������ҳ��ȡ���ɶ�ε���ֱ��ȫ������
			System.out.println("find:"+user100);
			System.out.println("find time taken in MS--"+(System.currentTimeMillis()-start));
			
			start = System.currentTimeMillis();
			//ValueFilter�������ַ����͡��������͡��������͵ȵĳ��ù��˲��������ַ���"��ʼ,����,����,������"���������ڵ�"���,��С,����"��
			CoolHashResult hr = chc.find("user.*.name", ValueFilter.contains("8888"), true);//��ѯ���ְ���88888�������û���like%88888%��
			CoolHashMap chmb = hr.nextBatch(50);
			System.out.println("find:"+chmb);
			System.out.println("find time taken in MS--"+(System.currentTimeMillis()-start));
			
			start = System.currentTimeMillis();
			CoolKeyResult kr = chc.findKey("user.*.age", ValueFilter.less(20l), true);//��ѯ����С��20����û�key
			CoolKeySet ks = kr.nextBatchKey(50);
			System.out.println("findKey:"+ks);
			ks = kr.nextBatchKey(50);
			System.out.println("findKey:"+ks);
			System.out.println("find time taken in MS--"+(System.currentTimeMillis()-start));
		}catch(Exception ex){
			ex.printStackTrace();
		}
		chc.exit();
	}
	
	public static void pointDemo(String[] args){
		CoolHashClient chc = BeanContext.getCoolHashClient(args[0],Integer.parseInt(args[1]));
		try{
			long start = System.currentTimeMillis();
			chc.put("aaa","aaa");
			chc.putPoint("bbb","aaa");
			chc.putPoint("ccc","bbb");
			System.out.println(chc.getPoint("ccc"));//ָ��aaa

			chc.put("order.0.price", 158.68d);
			chc.putPoint("order.0.buyer", "user.0");//ָ��ģ��key
			chc.put("user.0.name", "zhangsan");
			chc.putPoint("user.0.order.0", "order.0");
			System.out.println(chc.get("order.0.buyer"));//��ȡָ��key
			System.out.println(chc.getPoint("order.0.buyer",".name"));//����name���ԣ�ָ��user.0.name
			System.out.println(chc.getPoint("order.0.buyer",".order.0",".price"));//ָ��user.0.order.0��ָ��order.0.price
			
			System.out.println("time taken in MS--"+(System.currentTimeMillis()-start));
		}catch(Exception ex){
			ex.printStackTrace();
		}
		chc.exit();
	}
	
	public static void findPointDemo(String[] args){
		CoolHashClient chc = BeanContext.getCoolHashClient(args[0],Integer.parseInt(args[1]));
		try{
			long start = System.currentTimeMillis();
			CoolHashMap hm=new CoolHashMap();
			for(long i=0;i<100000;i++){
				hm.put("order."+i+".price", i);
				hm.putPoint("order."+i+".buyer", "user."+i);//orderͨ��keyָ�����user
				hm.put("user."+i+".name",i+"name");
				hm.putPoint("user."+i+".order."+i, "order."+i);//userͨ��keyָ�����order
			}
			System.out.println("load time taken in MS--"+(System.currentTimeMillis()-start));
			start = System.currentTimeMillis();
			int n=chc.put(hm);//����д���������
			System.out.println("put time taken in MS--"+(System.currentTimeMillis()-start));
			System.out.println("put total:"+n);
			
			start = System.currentTimeMillis();
			//��ѯorder�۸�����10������user
			CoolKeyResult kr = chc.findKey("user.*.order.*", ValueFilter.less(10l), true, ".price");
			CoolKeySet ks = kr.nextBatchKey(50);
			System.out.println("find time taken in MS--"+(System.currentTimeMillis()-start));
			System.out.println("findKey:"+ks);
			System.out.println("findKey total:"+ks.size());
			
			start = System.currentTimeMillis();
			//��ѯbuyer������8888name����������order
			CoolHashResult hr = chc.find("order.*.buyer", ValueFilter.endsWith("8888name"), true, ".name");
			CoolHashMap chmb = hr.nextBatch(50);
			System.out.println("find time taken in MS--"+(System.currentTimeMillis()-start));
			System.out.println("find:"+chmb);
			System.out.println("find total:"+chmb.size());
		}catch(Exception ex){
			ex.printStackTrace();
		}
		chc.exit();
	}
	
	public static void andOrDemo(String[] args){
		CoolHashClient chc = BeanContext.getCoolHashClient(args[0],Integer.parseInt(args[1]));
		try{
			long start = System.currentTimeMillis();
			CoolHashMap hm=new CoolHashMap();
			for(int i=0;i<100;i++){
				hm.put("person."+i+".name",i+"name");
				hm.put("person."+i+".age",i);
			}
			int n=chc.put(hm);//д��100����������
			
			//��ѯ������88name������person
			CoolKeyResult kn = chc.findKey("person.*", ValueFilter.endsWith("88name"), true);
			CoolKeySet ksn = kn.nextBatchKey(50);
			System.out.println("findKey:"+ksn);
			System.out.println("findKey total:"+ksn.size());
			
			//��ѯ�������60��person
			CoolKeyResult ka = chc.findKey("person.*", ValueFilter.greater(60), true);
			CoolKeySet ksa = ka.nextBatchKey(50);
			System.out.println("findKey:"+ksa);
			System.out.println("findKey total:"+ksa.size());
			
			//getSuperKeys(1)��ʾ��ȡ���ڶ����ĸ�key
			System.out.println("and:"+ksn.getSuperKeys(1).and(ksa.getSuperKeys(1)));//ȡ����
			System.out.println("or:"+ksn.getSuperKeys(1).or(ksa.getSuperKeys(1)));//ȡ��
			System.out.println("except:"+ksa.getSuperKeys(1).except(ksn.getSuperKeys(1)));//ȡ�
			System.out.println("time taken in MS--"+(System.currentTimeMillis()-start));
		}catch(Exception ex){
			ex.printStackTrace();
		}
		chc.exit();
	}
	
	public static void sortDemo(String[] args){
		CoolHashClient chc = BeanContext.getCoolHashClient(args[0],Integer.parseInt(args[1]));
		try{
			CoolHashMap hm=new CoolHashMap();
			for(int i=0;i<100;i++){
				hm.put(i+"", i);
			}
			int n=chc.put(hm);//����д��100����������
			hm = chc.get(hm.getKeys());//��������100����������
			System.out.println(hm);//��д��˳�����
			
			long start = System.currentTimeMillis();
			//Ĭ�ϰ�key��ĸ����
			Map.Entry[] enarr = hm.sort();
			System.out.println(Arrays.toString(enarr));
			
			//�Զ��尴key��ĸ����
			CoolKeySet<String> ks = hm.getKeys();
			String[] ksarr=ks.sort(new Comparator<String>(){
				public int compare(String o1, String o2){
					return o2.compareTo(o1);
				}
			});
			System.out.println(Arrays.toString(ksarr));
			
			//�Զ��尴value���ִ�С����
			Map.Entry[] arr=hm.sort(new Comparator<Map.Entry>(){
				public int compare(Map.Entry o1, Map.Entry o2){
					return (int)o1.getValue()-(int)o2.getValue();
				}
			});
			System.out.println(Arrays.toString(arr));
			System.out.println("sort time taken in MS--"+(System.currentTimeMillis()-start));
		}catch(Exception ex){
			ex.printStackTrace();
		}
		chc.exit();
	}
	
	public static void transDemo(String[] args){
		CoolHashClient chc = BeanContext.getCoolHashClient(args[0],Integer.parseInt(args[1]));
		try{
			chc.put("a","100");//a��ʼֵ
			chc.put("b","100");//b��ʼֵ
			chc.begin();//��ʼ�������
			chc.put("a","80");
			chc.remove("b");
			System.out.println("a:"+chc.get("a"));//�ع�ǰ���a���Ѿ��ı�
			System.out.println("b:"+chc.get("b"));//�ع�ǰ���b���Ѿ��ı�
			chc.put("b",new RunClient());//һ��������������ع�
			chc.commit();//�ύ����
		}catch(Exception ex){
			System.out.println(ex);
			chc.rollback();//�ع�����
		}
		
		try{
			System.out.println("a:"+chc.get("a"));//�ع������a���Ѿ���ԭ
			System.out.println("b:"+chc.get("b"));//�ع������b���Ѿ���ԭ
		}catch(Exception ex){
			System.out.println(ex);
		}
		chc.exit();
	}
	
	public static void main(String[] args){
		//���з�ʽ��2������Ϊ��Ҫ���ӵ�CoolHashServer��ip�Ͷ˿ڣ�: java  -cp fourinone.jar; RunClient localhost 2014
		singleDemo(args);//������дdemo
		//batchDemo(args);//������дdemo
		//findDemo(args);//��ѯ����demo
		//pointDemo(args);//keyָ��demo
		//findPointDemo(args);//keyָ���ѯdemo
		//andOrDemo(args);//and��or��except����demo
		//sortDemo(args);//����demo
		//transDemo(args);//����demo
	}
}