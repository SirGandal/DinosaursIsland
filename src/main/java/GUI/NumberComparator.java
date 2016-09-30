package GUI;

import java.util.Comparator;

@SuppressWarnings("rawtypes")
public class NumberComparator implements Comparator {

	@Override
	public int compare(Object o1, Object o2) {
		String s1 = (String) o1;
		String s2 = (String) o2;
		int number1 = Integer.parseInt(s1);
		int number2 = Integer.parseInt(s2);
		return number1 - number2;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj!=null){
			return obj.getClass().equals(this.getClass());
		}else{
			return false;
		}
	}
	
	public int hashCode() {
		  assert false : "hashCode not designed";
		  return 0;
		  }

}
