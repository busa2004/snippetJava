package testProject;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.reducing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


class A{

	public  String hash01(String[] participant, String[] completion) {
		String answer = "";
		HashMap<String, Integer> hm = new HashMap<>();
		for (String player : participant) hm.put(player, hm.getOrDefault(player,0) + 1);
		for (String player : participant) hm.put(player,  hm.get(player) - 1);
		for (String key : hm.keySet()) {
			if(hm.get(key) != 0) {
				answer = key;
			}
		}
		return answer;
	}
	
	public  boolean hash02(String[] phoneBook) {
		for(int i = 0 ; i < phoneBook.length - 1; i++) {
			for(int j=i+1; j<phoneBook.length; j++) {
				if(phoneBook[i].startsWith(phoneBook[j])) {return false;}
				if(phoneBook[j].startsWith(phoneBook[i])) {return false;}
			}
		}
		return true;
	}
	
	public  int hash03(String[][] clothes) {
		int mpa = Arrays.stream(clothes).collect(groupingBy(p->p[1], mapping(p -> p[0],counting()))).values()
				.stream().collect(reducing(1L,(x,y) -> x * (y+1))).intValue() - 1;
		System.out.println(mpa);
		return Arrays.stream(clothes)
					 .collect(groupingBy(p -> p[1], mapping(p -> p[0], counting())))
					 .values()
					 .stream()
					 .collect(reducing(1L,(x,y) -> x * (y + 1))).intValue() - 1;
	}
	
	public  int hash04(String[][] clothes) {
		int answer = 1;
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for(int i = 0 ; i <clothes.length; i++) {
			String key = clothes[i][1];
			if(!map.containsKey(key)) {
				map.put(key, 1);
			}else {
				map.put(key, map.get(key) +  1);
			}
		}
		
		Iterator<Integer> it = map.values().iterator();
		while(it.hasNext()) {
			answer *= it.next().intValue() + 1;
		}
		
		return answer - 1;
	}
	
	//hash05
	
	 class Music implements Comparable<Music>{
		
		private int played;
		private int id;
		private String genre;
		
		public String getGenre() {
			return this.genre;
		}
		public Music(String genre, int played, int id) {
			this.genre = genre;
			this.played = played;
			this.id = id;
		}
		
		@Override
		public int compareTo(Music other) {
			if(this.played == other.played) return this.id - other.id;
			return other.played - this.played;
		}
		
		@Override
		public String toString() {
			return "["+id +","+ played+","+ genre  + "]";
			
		}
	}
	public  int[] hash05(String[] genres, int[] plays) {
		int[] t = IntStream.range(0, genres.length).mapToObj(i -> new Music(genres[i],plays[i],i))
		.collect(Collectors.groupingBy(Music::getGenre)).entrySet().stream().sorted( (a,b) -> sum(b.getValue()) - sum(a.getValue()) )
		.flatMap(x -> x.getValue().stream().sorted().limit(2))
		.mapToInt(x->x.id).toArray();
		return t;
	}
	
	private  int sum(List<Music> value) {
		int answer = 0;
		for(Music music: value) {
			answer += music.played;
		}
		return answer;
	}
	
	
	public  int[] stackAndQueue01(int[] progresses, int[] speeds) {
		int[] dayOfend = new int[100];
		int day = -1;
		for (int i = 0 ; i < progresses.length; i++) {
			while(progresses[i] + (day*speeds[i]) < 100) {
				day++;
			}
			dayOfend[day]++;
		}
		return Arrays.stream(dayOfend).filter(i -> i!=0).toArray();
	}
	
	public  int[] stackAndQueue02(int[] progresses, int[] speeds) {
	        ArrayList<Integer> list = new ArrayList<>();

	        int temp = 0;
	        for (int i = 0; i < progresses.length; i++) {
	            int current = (100 - progresses[i]) / speeds[i];
	            if (temp < current) {
	                temp = current;
	                list.add(1);
	            } else list.set(list.size() - 1, list.get(list.size() - 1) + 1);
	        }

	        return list.stream().mapToInt(Integer::intValue).toArray();
	}
	
	public  int stackAndQueue03(int[] priorities, int location) {
		int answer = 0;
		int l = location;
		
		Queue<Integer> que = new LinkedList<Integer>();
		for(int i : priorities) {
			que.add(i);
		}
		
		Arrays.sort(priorities);
		int size = priorities.length - 1;
		
		while(!que.isEmpty()) {
			Integer i = que.poll();
			if(i == priorities[size - answer]) {
				answer++;
				l--;
				if(l<0)
					break;
			}else {
				que.add(i);
				if(l<0)
					break;
			}
		}
		return answer;
		
	}
	
    class Truck {
        int weight;
        int move;

        public Truck(int weight) {
            this.weight = weight;
            this.move = 1;
        }

        public void moving() {
            move++;
        }
    }

    public int stackAndQueue04(int bridgeLength, int weight, int[] truckWeights) {
        Queue<Truck> waitQ = new LinkedList<>();
        Queue<Truck> moveQ = new LinkedList<>();

        for (int t : truckWeights) {
            waitQ.offer(new Truck(t));
        }

        int answer = 0;
        int curWeight = 0;

        while (!waitQ.isEmpty() || !moveQ.isEmpty()) {
            answer++;

            if (moveQ.isEmpty()) {
                Truck t = waitQ.poll();
                curWeight += t.weight;
                moveQ.offer(t);
                continue;
            }

            for (Truck t : moveQ) {
                t.moving();
            }

            if (moveQ.peek().move > bridgeLength) {
                Truck t = moveQ.poll();
                curWeight -= t.weight;
            }

            if (!waitQ.isEmpty() && curWeight + waitQ.peek().weight <= weight) {
                Truck t = waitQ.poll();
                curWeight += t.weight;
                moveQ.offer(t);
            }
        }

        return answer;
    }
    
    public int[] stackAndQueue05(int[] prices) {
        int len = prices.length;
        int[] answer = new int[len];
        int i, j;
        for (i = 0; i < len; i++) {
            for (j = i + 1; j < len; j++) {
                answer[i]++;
                if (prices[i] > prices[j])
                    break;
            }
        }
        return answer;
    }
    
    public  int[] stackAndQueue06(int[] prices) {
        Stack<Integer> beginIdxs = new Stack<>();
        int i=0;
        int[] terms = new int[prices.length];

        beginIdxs.push(i);
        for (i=1; i<prices.length; i++) {
            while (!beginIdxs.empty() && prices[i] < prices[beginIdxs.peek()]) {
                int beginIdx = beginIdxs.pop();
                terms[beginIdx] = i - beginIdx;
            }
            beginIdxs.push(i);
        }
        while (!beginIdxs.empty()) {
            int beginIdx = beginIdxs.pop();
            terms[beginIdx] = i - beginIdx - 1;
        }

        return terms;
    }
    
    //scoville = [1, 2, 3, 9, 10, 12]
    //k = 7
    public int heap01(int[] scoville, int K) {
        PriorityQueue<Integer> q = new PriorityQueue<>();

        for(int i = 0; i < scoville.length; i++)
            q.add(scoville[i]);

        int count = 0;
        while(q.size() > 1 && q.peek() < K){
            int weakHot = q.poll();
            int secondWeakHot = q.poll();

            int mixHot = weakHot + (secondWeakHot * 2);
            q.add(mixHot);
            count++;
        }

        if(q.size() <= 1 && q.peek() < K)
            count = -1;

        return count;
    }
    
    //jobs = [[0, 3], [1, 9], [2, 6]]
    //return 9
    public  int heap02(int[][] jobs) {

        Arrays.sort(jobs, new Comparator<int[]>() {
            public int compare(int[] o1, int[] o2) {
                if(o1[0] <= o2[0]){
                    return -1;
                }
                return 1;
            }
        });      

        PriorityQueue<int[]> queue = new PriorityQueue<int[]>(new Comparator<int[]>() {
            public int compare(int[] o1, int[] o2) {
                if(o1[1] < o2[1]){
                    return -1;
                }
                return 1;
            }
        });

        int time = 0;
        int index = 0;
        float answer = 0;

        while(true){
            while(index < jobs.length && jobs[index][0] <= time){
                queue.offer(jobs[index]);
                index ++;
            }
            if(queue.size() == 0){
                time = jobs[index][0];
                continue;
            }
            int[] job = queue.poll();
            time += job[1];
            answer += time - job[0];
            if(index == jobs.length && queue.size() == 0){
                break;
            }
        }

        answer /= jobs.length;
        return (int)answer;
      }
    
    // operations = ["I 7","I 5","I -5","D -1"]	
    // return = [7,5]
    public  int[] heap03(String[] arguments){
    	
    	MidV q = new MidV();

        for(int i = 0; i < arguments.length; i++){
            String[] commend = arguments[i].split(" ");

            int v = Integer.parseInt(commend[1]);
            if(commend[0].equals("I")){
                q.push(v);
            }else{
                switch (v){
                    case 1 : q.removeMax();
                    break;
                    case -1: q.removeMin();
                    break;
                }
            }
        }


        int[] aw = new int[]{q.getMaxValue(),q.getMinValue()};

        return aw;
    	
    }
    
     class MidV{
        private PriorityQueue<Integer> leftHeap;
        private PriorityQueue<Integer> rightHeap;

        public MidV(){
            leftHeap = new PriorityQueue<>(10,Collections.reverseOrder());//최대값;
            rightHeap = new PriorityQueue<>();//최소값
        }


        public void push(int v){
            leftHeap.add(v);
        }

        public void removeMax(){

            while(!rightHeap.isEmpty()){
                leftHeap.add(rightHeap.poll());
            }

            leftHeap.poll();
        }

        public void removeMin(){

            while(!leftHeap.isEmpty()){
                rightHeap.add(leftHeap.poll());
            }

            rightHeap.poll();
        }

        public int getMaxValue(){

            if(leftHeap.size() == 0 && rightHeap.size() == 0)
                return 0;

            while(!rightHeap.isEmpty()){
                leftHeap.add(rightHeap.poll());
            }

            return leftHeap.peek();
        }

        public int getMinValue(){

            if(leftHeap.size() == 0 && rightHeap.size() == 0)
                return 0;

            while(!leftHeap.isEmpty()){
                rightHeap.add(leftHeap.poll());
            }

            return rightHeap.peek();
        }

    }
    
    // operations = ["I 7","I 5","I -5","D -1"]	
    // return = [7,5]
    public int[] heap04(String[] arguments) {
        int[] answer = {0,0};

        PriorityQueue<Integer> pq = new PriorityQueue<Integer>();
        PriorityQueue<Integer> reverse_pq = new PriorityQueue<Integer>(Collections.reverseOrder());

        for(int i=0; i<arguments.length; i++) {
            String temp[] = arguments[i].split(" ");
            switch(temp[0]) {
            case "I" : 
                pq.add(Integer.parseInt(temp[1]));
                reverse_pq.add(Integer.parseInt(temp[1]));
                break;
            case "D" :
                if(pq.size() > 0) {
                    if(Integer.parseInt(temp[1]) == 1) {
                        // 최댓값 삭제
                        int max = reverse_pq.poll();
                        pq.remove(max);
                    } else {
                        // 최솟값 삭제
                        int min = pq.poll();
                        reverse_pq.remove(min);
                    }
                }
                break;
            }
        }

        if(pq.size() >= 2) {
            answer[0] = reverse_pq.poll();
            answer[1] = pq.poll();
        }

        System.out.println(answer[0] + ":" + answer[1]);

        return answer;
    }
    
    public int[] sort01(int[] array, int[][] commands) {
        int[] answer = new int[commands.length];

        for(int i=0; i<commands.length; i++){
            int[] temp = Arrays.copyOfRange(array, commands[i][0]-1, commands[i][1]);
            Arrays.sort(temp);
            answer[i] = temp[commands[i][2]-1];
        }

        return answer;
    }
    
    // numbers = [6, 10, 2]
    // return = "6210"
    public  String sort02(int[] numbers) {
        String[] nums = new String[numbers.length];

        for (int i=0; i<nums.length; i++) 
            nums[i] = numbers[i] + "";

        Arrays.sort(nums, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return (o2 + o1).compareTo(o1 + o2);
            }
        });

        String ans = "";
        for (int i=0; i<numbers.length; i++)
            ans += nums[i];

        return ans.charAt(0) == '0' ? "0" : ans;
    }
    
    // citations = [3, 0, 6, 1, 5]
    // return = 3
    public  int sort03(int[] citations) {
        Arrays.sort(citations);

        int max = 0;
        for(int i = citations.length-1; i > -1; i--){
            int min = (int)Math.min(citations[i], citations.length - i);
            if(max < min) max = min;
        }

        return max;
    }
    
    // answers = [1,2,3,4,5]	
    // return [1]
    public int[] fullSearch01(int[] answer) {
        int[] a = {1, 2, 3, 4, 5};
        int[] b = {2, 1, 2, 3, 2, 4, 2, 5};
        int[] c = {3, 3, 1, 1, 2, 2, 4, 4, 5, 5};
        int[] score = new int[3];
        for(int i=0; i<answer.length; i++) {
            if(answer[i] == a[i%a.length]) {score[0]++;}
            if(answer[i] == b[i%b.length]) {score[1]++;}
            if(answer[i] == c[i%c.length]) {score[2]++;}
        }
        int maxScore = Math.max(score[0], Math.max(score[1], score[2]));
        ArrayList<Integer> list = new ArrayList<>();
        if(maxScore == score[0]) {list.add(1);}
        if(maxScore == score[1]) {list.add(2);}
        if(maxScore == score[2]) {list.add(3);}
        return list.stream().mapToInt(i->i.intValue()).toArray();
    }
    
    
    public  boolean isPrime(int n) {
    	if ( n == 0 || n == 1 ) return false;
    	for ( int i = 3; i < (int)Math.sqrt(n) ; i+=2 ) {
    		if(n%i==0) return false;
    	}
    	return true;
    }
    
    public  void permutation(String prefix, String str, HashSet<Integer> set) {
    	
    	int n = str.length();
    	
    	if(!prefix.equals("")) set.add(Integer.valueOf(prefix));
    	
    	for (int i = 0 ; i < n ; i++) {
    		permutation(prefix + str.charAt(i), str.substring(0,i) + str.substring(i+1, n), set);
    	}
    	
    }
    
    public int fullSearch02(String numbers) {
        HashSet<Integer> set = new HashSet<>();
        permutation("", numbers, set);
        int count = 0;
        while(set.iterator().hasNext()){
            int a = set.iterator().next();
            set.remove(a);
            if(a==2) count++;
            if(a%2!=0 && isPrime(a)){
                count++;
            }
        }        
        return count;
    }
    
    public int[] fullSearch03(int brown, int red) {
    	for(int i=1; i<=red; i++) {
    		if(red%i==0 && (red/i+i)*2+4==brown) {
    			return new int[] {red/i+2, i+2};
    		}
    	}
    	return null;
    }
    
    public int greedy01(int n, int[] lost, int[] reserve) {
    	
    	int[] people = new int[n];
    	int answer = n;
    	
    	for(int l : lost) 
    		people[l-1]--;
    	for(int r : reserve)
    		people[r-1]++;
    	
    	for(int i = 0 ; i< people.length ; i++) {
    		if(people[i] == -1) {
    			if(i-1>=0 && people[i-1] == 1) {
    				people[i]++;
    				people[i-1]--;
    			}else if(i+1 < people.length && people[i+1] == 1) {
    				people[i]++;
    				people[i+1]--;
    			}else {
    				answer--;
    			}
    		}
    	}
    	return answer;
    }
    
    // name = "AEAAAE"
    // return 56
    public int greedy02(String name) {
        int answer = 0;
        int[] diff={0,1,2,3,4,5,6,7,8,9,10,11,12,13,12,11,10,9,8,7,6,5,4,3,2,1};
        for(char c:name.toCharArray())
            answer+=diff[c-'A'];

        int length=name.length();
        int min=length-1;

        for(int i=0;i<length;i++){
            int next=i+1;
            while(next<length && name.charAt(next)=='A'){
                next++;
            }               
            int ln = length-next; //뒤의 거리
            int t1 = i+ln; // 앞의 거리 + 뒤의 거리 
            int t2 = Math.min(i,ln);// 앞의 거리, 뒤의 거리
            min=Math.min(min,t1 + t2);//그냥 쭉가기, 앞의 거리 + 뒤의 거리 + 돌아가는 거
        }

        return answer+min;
    }
    
    // number = "1924"
    // k = 2
    // return "94"
    public String greedy03(String number, int k) {
    	
    	char[] result = new char[number.length() - k];
    	Stack<Character> stack = new Stack<>();
    	
    	for(int i = 0 ; i < number.length() ; i++) {
    		char c = number.charAt(i);
    		while(!stack.isEmpty() && stack.peek() < c && k-- > 0) {
    			stack.pop();
    		}
    		stack.push(c);
    	}
    	
    	for(int i = 0 ; i < result.length ; i++) {
    		result[i] = stack.get(i);
    	}
    	
    	return new String(result);
    }
    
    // people = [70, 50, 80, 50]
    // limit = 100
    // return 3
    public int greedy04( int[] people, int limit)  {
    	
    	Arrays.sort(people);
    	int i = 0 ,j = people.length - 1;
    	for (;i < j ; j --) {
    		if(people[i] + people[j] < limit) {
    			++i;
    		}
    	}
    	return people.length - i; 
    }
    
    public int greedy05( int[] people, int limit)  {
    	
    	int answer = 0;
    	int n = people.length;
    	Arrays.sort(people);
    	
    	int i = n-1;
    	int j = 0;
    	
    	while( i > j ) {
    		if(people[i] + people[j] <= limit) {
    			i--;
    			j++;
    		}
    		else {
    			i--;
    		}
    	}
    	return answer = (n - j);
    }
    
    // greed(그리드), 그래프, 최소비용으로 간선 연결하기 union find
    // n = 4
    // costs = [[0,1,1],[0,2,2],[1,2,5],[1,3,1],[2,3,8]]
    public int greedy06(int n, int[][] costs)
    {
        int sum = 0;
        int[] island = new int[n];

        for(int i = 0; i < n; i++)
            island[i] = i;

        Arrays.sort(costs, (a, b) -> Integer.compare(a[2], b[2]));

        for(int i = 0; i < costs.length; i++)
        {
            if(find(island, costs[i][0]) != find(island, costs[i][1]))
            {
                unite(island, costs[i][0], costs[i][1]);
                sum += costs[i][2];
            }
        }

        return sum;
    }

    private int find(int[] island, int x)
    {
        if(island[x]== x)
            return x;
        return find(island, island[x]);
    }

    private void unite(int[] island, int x, int y)
    {
        int a = find(island, x);
        int b = find(island, y);
        island[a] = b;
    }
    
    // routes = [[-20,15], [-14,-5], [-18,-13], [-5,-3]]	
    // return 2
    public int greedy06(int[][] routes) {
    	Arrays.sort(routes, (a,b) -> Integer.compare(a[1],b[1]) );
    	int ans = 0;
    	int last_camera = Integer.MIN_VALUE;
    	for(int[] a : routes) {
    		if(last_camera < a[0]) {
    			++ans;
    			last_camera = a[1];
    		}
    	}
    	return ans;
    }
    
    // N = 5
    // number = 12
    // return = 4
    public int dp01(int N, int number) {
        int answer = -1;
         Set<Integer>[] setArr = new Set[9];
         int t = N;
         for(int i = 1; i < 9; i++) {
             setArr[i] = new HashSet<>();
             setArr[i].add(t);
             t = t * 10 + N;
         }
         for(int i = 1; i < 9; i++) {
             for(int j = 1; j < i; j++) {
                 for(Integer a : setArr[j]) {
                     for(Integer b : setArr[i - j]) {
                         setArr[i].add(a + b);
                         setArr[i].add(a - b);
                         setArr[i].add(b - a);
                         setArr[i].add(a * b);
                         if(b != 0) {
                             setArr[i].add(a / b);
                         }
                         if(a != 0) {
                             setArr[i].add(b / a);
                         }
                     }
                 }
             }
         }
         for(int i = 1; i < 9; i++) {
             if(setArr[i].contains(number)) {
                 answer = i;
                 break;
             }
         }
         return answer;
     }
    
    // triangle = [[7], [3, 8], [8, 1, 0], [2, 7, 4, 4], [4, 5, 2, 6, 5]]
    // result = 30
    public int dp02(int[][] triangle) {
    	
    	 for (int i = 1; i < triangle.length; i++) {
             triangle[i][0] += triangle[i-1][0];
             triangle[i][i] += triangle[i-1][i-1];
             for (int j = 1; j < i; j++) 
                 triangle[i][j] += Math.max(triangle[i-1][j-1], triangle[i-1][j]);
         }
        
    	 return Arrays.stream(triangle[triangle.length-1]).max().getAsInt();
    }
    
    // m = 4
    // n = 3
    // puddles = [[2,2]]
    // return = 4
    public int dp03(int m, int n, int[][] puddles) {
        int[][] dp = new int[m+1][n+1];
        for(int i=0;i<puddles.length;i++){
            dp[puddles[i][0]][puddles[i][1]]=-1;
        }
        dp[1][1]=1; 
        for(int i=1;i<=m;i++){
            for(int j=1;j<=n;j++){
                if(dp[i][j]==-1){
                    dp[i][j]=0;
                    continue;
                }
                if(i!=1)    dp[i][j]+=dp[i-1][j]%1000000007;
                if(j!=1)    dp[i][j]+=dp[i][j-1]%1000000007;
            }
        }
        return dp[m][n]%1000000007;
    }
    
    // money = [1, 2, 3, 1]
    // return = 4
    public int dp04(int[] money) {        
        int[][] pick = new int[2][money.length];

        //처음부터 시작 첫집을 털면 두번째 집과 마지막 집은 털 없음  
        pick[0][0] = money[0];
        pick[0][1] = money[0];
        
        //두번째부터 시작 두번째 집을 털면 첫번째집은 털수 없으며 마지막 집은 털수 있음  
        pick[1][0] = 0;
        pick[1][1] = money[1];

        for(int i=2; i<money.length; i++) {
            pick[0][i] = Math.max(pick[0][i-2] + money[i], pick[0][i-1]);
            pick[1][i] = Math.max(pick[1][i-2] + money[i], pick[1][i-1]);
        }

        return Math.max(pick[0][pick[0].length-2], pick[1][pick[1].length-1]);
    }
    
    // numbers = [1,1,1,1,1]
    // target = 3
    // return = 5
    // 모든 경우의 수 합계 구하기 
    // numbers길이 만큼 숫자를 더하거나 빼고 결과를 target과 비교한다. 
    // 재귀를 이용한 dfs
    // 합계 같은 횟수 구하기 
    public int dfsAndBfs01(int[] numbers, int target) {
    	int answer = 0;
        answer = dfs(numbers, 0, 0, target);
        return answer;
    }
    
    public int dfs(int[] numbers, int n, int sum, int target) {
        if(n == numbers.length) {
            if(sum == target) {
                return 1;
            }
            return 0;
        }
        return dfs(numbers, n + 1, sum + numbers[n], target) + dfs(numbers, n + 1, sum - numbers[n], target);
    }
    
    // n = 3
    // computers = [[1, 1, 0], [1, 1, 0], [0, 0, 1]]
    // return 2
    // for문을 이용한 dfs 
    // dfs 덩어리 몇갠 구하기 
    public int dfsAndBfs02(int n, int[][] computers) {
        int answer = 0;
        boolean[] chk = new boolean[n];
        for(int i = 0; i < n; i++) {
            if(!chk[i]) {
                dfs(computers, chk, i);
                answer++;
            }
        }
        return answer;
    }
    void dfs(int[][] computers, boolean[] chk, int start) {
        chk[start] = true;
        for(int i = 0; i < computers.length; i++) {
            if(computers[start][i] == 1 && !chk[i]) {
                dfs(computers, chk, i);
            }
        }
    }
    
    // 단어변환
    // begin = "hit"			
    // target = "cog"			
    // words = ["hot", "dot", "dog", "lot", "log", "cog"]
    // return = 4
    // 큐로 횟수 구하기
    static class Node {
        String next;
        int edge;

        public Node(String next, int edge) {
            this.next = next;
            this.edge = edge;
        }
        
        @Override
        public String toString() {
        	return "next : " + next + " , edge : " + edge;
        }
    }
    
    static boolean isNext(String cur, String n) {
        int cnt = 0;
        for (int i=0; i<n.length(); i++) {
            if (cur.charAt(i) != n.charAt(i)) {
                if (++ cnt > 1) return false;
            }
        }

        return true;
    }  
    
    public int dfsAndBfs03(String begin, String target, String[] words) {
        int n = words.length, ans = 0;

        Queue<Node> q = new LinkedList<>();

        boolean[] visit = new boolean[n];
        q.add(new Node(begin, 0));

        while(!q.isEmpty()) {
            Node cur = q.poll();
            System.out.println(cur);
            if (cur.next.equals(target)) {
                ans = cur.edge;
                break;
            }

            for (int i=0; i<n; i++) {
                if (!visit[i] && isNext(cur.next, words[i])) {
                    visit[i] = true;
                    q.add(new Node(words[i], cur.edge + 1));
                }
            }
        }

        return ans;
    }
    
    

    
	
}

public class testClass {
	public static void main(String[] args) 
	{	
		A a = new A();
		String begin = "hit";
		String target = "cog";
		String[] words = {"hot", "dot", "dog", "lot", "log", "cog"};
		System.out.println(a.dfsAndBfs03(begin, target, words));
	}
}
