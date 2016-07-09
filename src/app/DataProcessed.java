package app;

public class DataProcessed implements Comparable<DataProcessed> {

	private Float yDirAdj;
	private String line;
	
	public DataProcessed(Float yDirAdj, String line) {
		super();
		this.line = line;
		this.yDirAdj = yDirAdj;
	}

	@Override
	public int compareTo(DataProcessed o) {
		return yDirAdj.compareTo(o.getyDirAdj());
	}
	
	public Float getyDirAdj() {
		return yDirAdj;
	}

	public void setyDirAdj(Float yDirAdj) {
		this.yDirAdj = yDirAdj;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}
	
	public void addCharacterToLine(String character) {
		this.line += character;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((yDirAdj == null) ? 0 : yDirAdj.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataProcessed other = (DataProcessed) obj;
		if (yDirAdj == null) {
			if (other.yDirAdj != null)
				return false;
		} else if (!yDirAdj.equals(other.yDirAdj))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.yDirAdj + ": " + this.line;
	}
}
