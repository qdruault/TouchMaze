package position;

/**
 * Created by Thibault on 22/05/2017.
 */
    
    
public class Position1D implements Position{
    	
        public int x;

        public Position1D(int x) {
            this.x = x;
        }

        public Position toRight() {
            x+=1;
            return this;
        }

        public Position toLeft() {
            x-=1;
            return this;
        }

        public Position toFront() {
            return this;
        }

        public Position toRear() {
            return this;
        }

        public Position toTop() {
            return this;
        }

        public Position toBot() {
            return this;
        }


		@Override
		public boolean is(Position p) {
			if(p.getClass() == getClass()){
				Position1D that = (Position1D) p;
				if(that.x == x){
					return true;
				}
			}
			return false;
		}
		
		@Override
		public String toString(){
			return "Position[x = " + x + "]";
		}
		
    }

