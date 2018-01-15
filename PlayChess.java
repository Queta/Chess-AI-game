//package chess;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class PlayChess extends JFrame{
	public static void main(String[] args)
	{
		PlayChess frame=new PlayChess();
	    frame.setTitle("Play it now!");
	    frame.setLocationRelativeTo(null);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setVisible(true);
	}
	
	public static int size=500;
	private int[] chessIndex={9,11,10,8,7,10,11,9,12,12,12,12,12,12,12,12,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,6,6,6,6,6,6,6,6,3,5,4,2,1,4,5,3};
	private final int[] initIndex={9,11,10,8,7,10,11,9,12,12,12,12,12,12,12,12,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,6,6,6,6,6,6,6,6,3,5,4,2,1,4,5,3};
	private String[] symbol={"White King","White Queen","White Rook","White Bishop","White Knight","White Pawn","Black King","Black Queen","Black Rook","Black Bishop","Black Knight","Black Pawn"};
	private boolean isChosen=false;
	private int choseIdx=0, lstIdx=-1, lstIdx2;
	private boolean wShort=true,wLong=true,bShort=true,bLong=true;//true王车均未动过
	private boolean whitePassBy=false,blackPassBy=false;
	private boolean whitePrmt=false,blackPrmt=false;
	private int prmtIdx=-1;//升变处
	private boolean Go=true;
	private int ifType=0;//type of interface, 0 for initial
	private boolean whiteWin=false;
	private JPanel playPanel=new JPanel();
	private JPanel chosePanel=new JPanel();
	private JPanel winPanel=new JPanel();
	private ImageIcon[] chessIcon=new ImageIcon[12];
	private JButton[] chessButton=new JButton[64];
	private JButton playAgain=new JButton("Play Again");
	private boolean[] access=new boolean[64];
	private Color[] chzColor={new Color(255,125,255),new Color(125,0,125)};//white first
	
	private PlayChess()
	{
		display();
		ifType=1;
		playAgain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				getContentPane().removeAll();
				ifType=0;
				display();
				ifType=1;
            }
        });
		tempChessIdx=new int[stepNumAI][64];
		tempAccess=new boolean[stepNumAI][64];
	}
	
	private void setChess(int[] chessIndex,int a,int b,boolean ai)//change chessIndex
	{
		if(b>=0)
		{
			chessIndex[b]=chessIndex[a];
			chessIndex[a]=0;
			if(chessIndex[b]==6&&b/8==0)
			{
				if(ai)
					chessIndex[b]=2;
				else
				{
					prmtIdx=b;
					callChosePanel();
				}
			}
			else if(chessIndex[b]==12&&b/8==7)
			{
				if(ai)
					chessIndex[b]=8;
				else
				{
					prmtIdx=b;
					callChosePanel();
				}
			}
		}
		else if(b==-1)
			chessIndex[a]=0;//消子
	}

	private void callChosePanel()
	{
		ifType=2;
		display();
	}
	
	private void isAccess(int[] chessIndex,int choseIdx)
	{
		for(int i=0;i<64;i++)
			access[i]=false;
		int row=choseIdx/8, col=choseIdx%8;
		
		//Knight
		if(chessIndex[choseIdx]==5||chessIndex[choseIdx]==11)
		{
			int[] a={-2,-2,-1,-1,1,1,2,2};
			int[] b={-1,1,-2,2,-2,2,-1,1};
			for(int i=0;i<8;i++)
			{
				int index=8*(row+a[i])+col+b[i];
				if(row+a[i]>=0&row+a[i]<8&&col+b[i]>=0&col+b[i]<8)
					access[index]=Go?(chessIndex[index]<1||chessIndex[index]>=7):(chessIndex[index]<7);
			}
		}
		
		//Queen & Rook
		if(chessIndex[choseIdx]==2||chessIndex[choseIdx]==8||chessIndex[choseIdx]==3||chessIndex[choseIdx]==9)
		{
			for(int i=row-1;i>=0;i--)//北
			{
				int index=i*8+col;
				if(chessIndex[index]==0)
					access[index]=true;
				else
				{
					access[index]=Go?(chessIndex[index]<1||chessIndex[index]>=7):(chessIndex[index]<7);
					break;
				}
			}
			
			for(int i=col+1;i<=7;i++)//东
			{
				int index=row*8+i;
				if(chessIndex[index]==0)
					access[index]=true;
				else
				{
					access[index]=Go?(chessIndex[index]<1||chessIndex[index]>=7):(chessIndex[index]<7);
					break;
				}
			}
			
			for(int i=row+1;i<=7;i++)//南
			{
				int index=i*8+col;
				if(chessIndex[index]==0)
					access[index]=true;
				else
				{
					access[index]=Go?(chessIndex[index]<1||chessIndex[index]>=7):(chessIndex[index]<7);
					break;
				}
			}
			
			for(int i=col-1;i>=0;i--)//西
			{
				int index=row*8+i;
				if(chessIndex[index]==0)
					access[index]=true;
				else
				{
					access[index]=Go?(chessIndex[index]<1||chessIndex[index]>=7):(chessIndex[index]<7);
					break;
				}
			}
		}
		
		//Queen & Bishop
		if(chessIndex[choseIdx]==2||chessIndex[choseIdx]==8||chessIndex[choseIdx]==10||chessIndex[choseIdx]==4)
		{
			for(int i=row-1;i>=0&&i+col>=row;i--)//左上
			{
				int index=i*9+col-row;
				if(chessIndex[index]==0)
					access[index]=true;
				else
				{
					access[index]=Go?(chessIndex[index]<1||chessIndex[index]>=7):(chessIndex[index]<7);
					break;
				}
			}
			
			for(int i=row-1;i>=0&&i>=row+col-7;i--)//右上
			{
				int index=i*7+col+row;
				if(chessIndex[index]==0)
					access[index]=true;
				else
				{
					access[index]=Go?(chessIndex[index]<1||chessIndex[index]>=7):(chessIndex[index]<7);
					break;
				}
			}
			
			for(int i=row+1;i<=7&&i<=row-col+7;i++)//右下
			{
				int index=i*9+col-row;
				if(chessIndex[index]==0)
					access[index]=true;
				else
				{
					access[index]=Go?(chessIndex[index]<1||chessIndex[index]>=7):(chessIndex[index]<7);
					break;
				}
			}
			
			for(int i=row+1;i<=7&&i<=row+col;i++)//左下
			{
				int index=i*7+col+row;
				if(chessIndex[index]==0)
					access[index]=true;
				else
				{
					access[index]=Go?(chessIndex[index]<1||chessIndex[index]>=7):(chessIndex[index]<7);
					break;
				}
			}
		}
		
		//Pawn
		if(chessIndex[choseIdx]==12||chessIndex[choseIdx]==6)
		{
			int index1=Go?Math.max(choseIdx-8,0):Math.min(choseIdx+8,63);//straight 1
			access[index1]=(chessIndex[index1]==0);
			
			if((Go&&choseIdx/8==6)||(!Go&&choseIdx/8==1))//straight 2
			{
				int index2=Go?Math.max(choseIdx-16,0):Math.min(choseIdx+16,63);
				access[index2]=(chessIndex[index1]==0&&chessIndex[index2]==0);
			}
			
			//diagonal
			int row2=Go?row-1:row+1;
			int index=8*row2+col-1;
			if(row2>=0&row2<8&&col-1>=0&col-1<8)
			{
				access[index]=Go?(chessIndex[index]>=7):(chessIndex[index]>0&&chessIndex[index]<7);
				if(whitePassBy||blackPassBy)
					if(Go?(chessIndex[index+8]==12&&(index+8)/8==3):(chessIndex[index-8]==6&&(index-8)/8==4))//passBy
						access[index]=true;
			}
			index=8*row2+col+1;
			if(row2>=0&row2<8&&col+1>=0&col+1<8)
			{
				access[index]=Go?(chessIndex[index]>=7):(chessIndex[index]>0&&chessIndex[index]<7);
				if(whitePassBy||blackPassBy)
					if(Go?(chessIndex[index+8]==12&&(index+8)/8==3):(chessIndex[index-8]==6&&(index-8)/8==4))//passBy
						access[index]=true;
			}
		}
		
		//King
		if(chessIndex[choseIdx]==1||chessIndex[choseIdx]==7)
		{
			for(int i=Math.max(row-1,0);i<=Math.min(row+1,7);i++)
				for(int j=Math.max(col-1,0);j<=Math.min(col+1,7);j++)
				{
					int index=i*8+j;
					access[index]=Go?(chessIndex[index]<1||chessIndex[index]>=7):(chessIndex[index]<7);
				}
			if(((Go&&wShort)||(!Go&&bShort))&&choseIdx+2<64)//0-0
				if(chessIndex[choseIdx+2]==0&&chessIndex[choseIdx+1]==0&&isSafe(chessIndex[choseIdx]<7,choseIdx)&&isSafe(chessIndex[choseIdx]<7,choseIdx+2)&&isSafe(chessIndex[choseIdx]<7,choseIdx+1))
					access[choseIdx+2]=true;
			if((Go&&wLong)||(!Go&&bLong)&&choseIdx>=3)//0-0-0
				if(chessIndex[choseIdx-3]==0&&chessIndex[choseIdx-2]==0&&chessIndex[choseIdx-1]==0&&isSafe(chessIndex[choseIdx]<7,choseIdx)&&isSafe(chessIndex[choseIdx]<7,choseIdx-2)&&isSafe(chessIndex[choseIdx]<7,choseIdx-1))
					access[choseIdx-2]=true;
		}
	}
	
	private boolean isSafe(boolean Go,int idx)
	{
		for(int pos=0;pos<64;pos++)
		{
			if(Go?chessIndex[pos]<7:(chessIndex[pos]==0||chessIndex[pos]>=7))
			{
				//System.out.print("-->"+chessIndex[pos]);
				continue;
			}
			
			int row=pos/8, col=pos%8;
			
			//Knight
			if(chessIndex[pos]==5||chessIndex[pos]==11)
			{
				int[] a={-2,-2,-1,-1,1,1,2,2};
				int[] b={-1,1,-2,2,-2,2,-1,1};
				for(int i=0;i<8;i++)
				{
					int index=8*(row+a[i])+col+b[i];
					if(row+a[i]>=0&row+a[i]<8&&col+b[i]>=0&col+b[i]<8)
						if(index==idx)
							return false;
				}
			}
			
			//Queen & Rook
			if(chessIndex[pos]==2||chessIndex[pos]==8||chessIndex[pos]==3||chessIndex[pos]==9)
			{
				for(int i=row-1;i>=0;i--)//北
				{
					int index=i*8+col;
					if(chessIndex[index]!=0&&index!=idx)
						break;
					if(index==idx)
						return false;
				}
				
				for(int i=col+1;i<=7;i++)//东
				{
					int index=row*8+i;
					if(chessIndex[index]!=0&&index!=idx)
						break;
					if(index==idx)
						return false;
				}
				
				for(int i=row+1;i<=7;i++)//南
				{
					int index=i*8+col;
					if(chessIndex[index]!=0&&index!=idx)
						break;
					if(index==idx)
						return false;
				}
				
				for(int i=col-1;i>=0;i--)//西
				{
					int index=row*8+i;
					if(chessIndex[index]!=0&&index!=idx)
						break;
					if(index==idx)
						return false;
				}
			}
			
			//Queen & Bishop
			if(chessIndex[pos]==2||chessIndex[pos]==8||chessIndex[pos]==10||chessIndex[pos]==4)
			{
				for(int i=row-1;i>=0&&i+col>=row;i--)//左上
				{
					int index=i*9+col-row;
					if(chessIndex[index]!=0&&index!=idx)
						break;
					if(index==idx)
						return false;
				}
				
				for(int i=row-1;i>=0&&i>=row+col-7;i--)//右上
				{
					int index=i*7+col+row;
					if(chessIndex[index]!=0&&index!=idx)
						break;
					if(index==idx)
						return false;
				}
				
				for(int i=row+1;i<=7&&i<=row-col+7;i++)//右下
				{
					int index=i*9+col-row;
					if(chessIndex[index]!=0&&index!=idx)
						break;
					if(index==idx)
						return false;
				}
				
				for(int i=row+1;i<=7&&i<=row+col;i++)//左下
				{
					int index=i*7+col+row;
					if(chessIndex[index]!=0&&index!=idx)
						break;
					if(index==idx)
						return false;
				}
			}
			
			//King
			if(chessIndex[pos]==1||chessIndex[pos]==7)
				for(int i=Math.max(row-1,0);i<=Math.min(row+1,7);i++)
					for(int j=Math.max(col-1,0);j<=Math.min(col+1,7);j++)
					{
						int index=i*8+j;
						if(index==idx)
							return false;
					}
		}
		//Pawn
		if(Go)
		{
			if(chessIndex[idx-9]==12||chessIndex[idx-7]==12)
				return false;
		}
		else
		{
			if(chessIndex[idx+9]==6||chessIndex[idx+7]==6)
				return false;
		}
		return true;
	}
	
	private class chosePrmt implements ActionListener
	{
		private int idx;//升变成为
		public chosePrmt(int i)
		{
			idx=i;
		}
		public void actionPerformed(ActionEvent e)
		{
			chessIndex[prmtIdx]=idx;
			ifType=1;
			remove(chosePanel);
			add(playPanel);
			display();
		}
	}
	
	private class Move implements ActionListener
	{
		private int idx;
		public Move(int i)
		{
			idx=i;
		}
		
		public void actionPerformed(ActionEvent e)
		{
			if(isChosen&&ifType!=3)
			{
				if(access[idx])
				{
					isChosen=false;
					lstIdx=idx;
					lstIdx2=choseIdx;
					if(chessIndex[idx]==1||chessIndex[idx]==7)//win
					{
						whiteWin=(chessIndex[idx]==7);
						setChess(chessIndex,choseIdx,idx,!Go);
						display();
						ifType=3;
						display();
					}
					else
					{
						//王车易位
						if((chessIndex[choseIdx]==1&&choseIdx==60)||(chessIndex[choseIdx]==3&&choseIdx==63))
							wShort=false;
						if((chessIndex[choseIdx]==1&&choseIdx==60)||(chessIndex[choseIdx]==3&&choseIdx==56))
							wLong=false;
						if((chessIndex[choseIdx]==7&&choseIdx==4)||(chessIndex[choseIdx]==9&&choseIdx==7))
							bShort=false;
						if((chessIndex[choseIdx]==7&&choseIdx==4)||(chessIndex[choseIdx]==9&&choseIdx==0))
							bLong=false;
						
						if((chessIndex[choseIdx]==1&&choseIdx==60)&&idx==62)
						{
							setChess(chessIndex,63,61,false);
							wShort=false;
							wLong=false;
						}
						if((chessIndex[choseIdx]==1&&choseIdx==60)&&idx==58)
						{
							setChess(chessIndex,56,59,false);
							wShort=false;
							wLong=false;
						}
						if((chessIndex[choseIdx]==7&&choseIdx==4)&&idx==6)
						{
							setChess(chessIndex,7,5,false);
							bShort=false;
							bLong=false;
						}
						if((chessIndex[choseIdx]==7&&choseIdx==4)&&idx==2)
						{
							setChess(chessIndex,0,3,false);
							bShort=false;
							bLong=false;
						}
						//Pawn pass by
						if(whitePassBy||blackPassBy)
							if(Go?(chessIndex[choseIdx]==6&&chessIndex[idx+8]==12&&(idx+8)/8==3):(chessIndex[choseIdx]==12&&chessIndex[idx-8]==6&&(idx-8)/8==4))//passBy
								setChess(chessIndex,Go?idx+8:idx-8,-1,false);
						if(Go&&idx/8==4)
							whitePassBy=true;
						if(!Go&&idx/8==3)
							blackPassBy=true;
						if(Go&&blackPassBy)
							blackPassBy=false;
						if(!Go&&whitePassBy)
							whitePassBy=false;
						
						//Pawn promotion in setChess
						if(Go)
							setChess(chessIndex,choseIdx,idx,false);//发生在chosePrmt之前
						else
							setChess(chessIndex,choseIdx,idx,true);//AI
						display();
						Go=!Go;
						if(!Go)
						{
							chessAI();
						}
						display();
					}
				}
				else
				{
					isChosen=false;
					//若选的是本方棋子则换成该棋子为待下棋子
					if(Go?chessIndex[idx]>=1&&chessIndex[idx]<=6:chessIndex[idx]>=7)
					{
						choseIdx=idx;
						isChosen=true;
						isAccess(chessIndex,choseIdx);
					}
					display();
				}
			}
			else
			{
				if(Go?chessIndex[idx]>=1&&chessIndex[idx]<=6:chessIndex[idx]>=7)
				{
					choseIdx=idx;
					isChosen=true;
					isAccess(chessIndex,choseIdx);
				}
			}
			display();
		}
	}
	
	private void display()
	{
		switch(ifType){
		case 0:
			isChosen=false;
			Go=true;
			whitePassBy=false;
			blackPassBy=false;
			whitePrmt=false;
			blackPrmt=false;
			wShort=true;
			wLong=true;
			bShort=true;
			bLong=true;
			lstIdx=-1;
			for(int i=0;i<64;i++)
				chessIndex[i]=initIndex[i];
			
			//初始化棋盘
			setSize(size,size);
			setLayout(new GridLayout(1,1));
			playPanel.removeAll();
			playPanel.setLayout(new GridLayout(8,8));
			playPanel.setSize(new Dimension(size,size));
			for(int i=0;i<12;i++)
				chessIcon[i]=new ImageIcon(String.valueOf(i+1)+".png");
			for(int i=0;i<64;i++)
			{
				chessButton[i]=new JButton();
				chessButton[i].setBackground((i/8-i%8)%2==0?Color.WHITE:Color.BLACK);
				chessButton[i].addActionListener(new Move(i));
				if(chessIndex[i]!=0)
				{
					chessButton[i].setIcon(chessIcon[chessIndex[i]-1]);
					chessButton[i].setToolTipText(symbol[chessIndex[i]-1]);
				}
				playPanel.add(chessButton[i]);
			}
			add(playPanel);
			break;
		case 1:
			//更新棋盘
			for(int i=0;i<64;i++)
			{
				if(chessIndex[i]!=0)
				{
					chessButton[i].setIcon(chessIcon[chessIndex[i]-1]);
					chessButton[i].setDisabledIcon(chessIcon[chessIndex[i]-1]);
					chessButton[i].setToolTipText(symbol[chessIndex[i]-1]);
				}
				else
				{
					chessButton[i].setIcon(null);
					chessButton[i].setToolTipText(null);
				}
				
				if(isChosen&&access[i])
					chessButton[i].setBackground((i/8-i%8)%2==0?chzColor[0]:chzColor[1]);
				else
					chessButton[i].setBackground((i/8-i%8)%2==0?Color.WHITE:Color.BLACK);
				if(isChosen)	
					chessButton[choseIdx].setBackground(Color.CYAN);
				else if(lstIdx>=0)
				{
					chessButton[lstIdx].setBackground((lstIdx/8-lstIdx%8)%2==0?Color.LIGHT_GRAY:Color.DARK_GRAY);
					chessButton[lstIdx2].setBackground((lstIdx2/8-lstIdx2%8)%2==0?Color.LIGHT_GRAY:Color.DARK_GRAY);
				}
				
				if((chessIndex[i]==1&&!(isSafe(true,i)))||(chessIndex[i]==7&&!(isSafe(false,i))))
					chessButton[i].setBackground(Color.RED);
			}
			repaint();
			break;
		case 2:
			//设置升变选择面板
			chosePanel.removeAll();
			chosePanel.setLayout(new GridLayout(5,1));
			//chosePanel.setSize(size,size);
			JLabel l1=new JLabel("Which one do you want to become?");
			l1.setHorizontalAlignment(SwingConstants.CENTER);
			JButton l2=new JButton(new ImageIcon((prmtIdx/8==0)?"2.png":"8.png"));//Queen
			l2.setToolTipText("Queen");
			l2.addActionListener(new chosePrmt((prmtIdx/8==0)?2:8));
			JButton l3=new JButton(new ImageIcon((prmtIdx/8==0)?"3.png":"9.png"));//Rook
			l3.setToolTipText("Rook");
			l3.addActionListener(new chosePrmt((prmtIdx/8==0)?3:9));
			JButton l4=new JButton(new ImageIcon((prmtIdx/8==0)?"4.png":"10.png"));//Bishop
			l4.setToolTipText("Bishop");
			l4.addActionListener(new chosePrmt((prmtIdx/8==0)?4:10));
			JButton l5=new JButton(new ImageIcon((prmtIdx/8==0)?"5.png":"11.png"));//Knight
			l5.setToolTipText("Knight");
			l5.addActionListener(new chosePrmt((prmtIdx/8==0)?5:11));
			chosePanel.add(l1);
			chosePanel.add(l2);
			chosePanel.add(l3);
			chosePanel.add(l4);
			chosePanel.add(l5);
			
			remove(playPanel);
			add(chosePanel);
			setVisible(true);
			break;
		case 3:
			//设置胜利面板
			remove(playPanel);
			setSize(2*size,size);
			//setPreferredSize(new Dimension(2*size,size));
			setLayout(new GridLayout(1,2));
			winPanel.removeAll();
			winPanel.setLayout(new BorderLayout(0,0));
			winPanel.setSize(size,size);
			JLabel img;
			JLabel txt=new JLabel();
			
			if(whiteWin)
			{
				img=new JLabel(new ImageIcon("win.gif"));
				txt.setText("YOU WIN!");
			}
			else
			{
				img=new JLabel(new ImageIcon("lose.gif"));
				txt.setText("YOU LOSE!");
			}
			img.setPreferredSize(new Dimension(size*3/5,size*4/5));
			img.setOpaque(true);
			img.setBackground(Color.ORANGE);
			txt.setFont(new Font("Georgia",Font.PLAIN,size/10));
			txt.setHorizontalAlignment(SwingConstants.CENTER);
			txt.setOpaque(true);
			txt.setBackground(Color.WHITE);
			winPanel.add(img, BorderLayout.NORTH);
			winPanel.add(txt, BorderLayout.CENTER);
			winPanel.add(playAgain,BorderLayout.EAST);
			
			for(int i=0;i<64;i++)
				chessButton[i].setEnabled(false);
			add(playPanel);
			add(winPanel);
		}
		repaint();
	}
	
	private final static double[] chessPieceValue={0.0,-99999.0,-9.0,-5.0,-3.25,-3.0,-1.0,99999.0,9.0,5.0,3.25,3.0,1.0};
	private final static double[] pawnNearTo8={1.0,3.5,1.4,0.15,0.1,0.05,0.0};
	private int xAI,yAI;
	private int stepNumAI=4;
	private int[][] tempChessIdx;
	private boolean[][] tempAccess;
	private final static double MININF=-99999999.0;
	
	private void chessAI()
	{
		for(int i=0;i<64;i++)
			chessButton[i].setEnabled(false);
		xAI=-1;
		yAI=-1;
		//AI begin to get xAI yAI
		
		//xAI=0;
		//yAI=0;
		
		search(0,MININF,-MININF);

		//AI finish and chess to player
		Go=false;
		for(int i=0;i<64;i++)
			chessButton[i].setEnabled(true);
		if(xAI==-1||yAI==-1)
			System.out.println("???");
		//for(int i=0;i<stepNumAI;i++)
		//	System.out.printf("%.0f\t",testScore[i]);
		//System.out.print("\n");
		chessButton[xAI].doClick();
		chessButton[yAI].doClick();
	}
	private double search(int stepNum,double alpha,double beta)
	{
		if(stepNum==stepNumAI)
		{
			double t=0.0;
			for(int i=0;i<64;i++)
				t+=chessPieceValue[tempChessIdx[stepNum-1][i]];
			for(int i=0;i<64;i++)
				if(tempChessIdx[stepNum-1][i]==8||tempChessIdx[stepNum-1][i]==9||tempChessIdx[stepNum-1][i]==10)
				{
					Go=(stepNum%2==1);
					isAccess(tempChessIdx[stepNum-1],i);
					for(int j=0;j<64;j++)
						if(access[j])
							t+=tempChessIdx[stepNum-1][i]==10?0.02:0.045;
				}
				else if(tempChessIdx[stepNum-1][i]==2||tempChessIdx[stepNum-1][i]==3||tempChessIdx[stepNum-1][i]==4)
				{
					Go=(stepNum%2==1);
					isAccess(tempChessIdx[stepNum-1],i);
					for(int j=0;j<64;j++)
						if(access[j])
							t-=tempChessIdx[stepNum-1][i]==5?0.02:0.045;
				}
				else if(tempChessIdx[stepNum-1][i]==12)
					t+=pawnNearTo8[7-i/8];
				else if(tempChessIdx[stepNum-1][i]==6)
					t-=pawnNearTo8[i/8];
			return t+Math.random()*0.7;
		}
		double tempScore=MININF;
		for(int x=0;x<64;x++)
		{
			/*if(stepNum==0)
				for(int i=0;i<64;i++)
					tempChessIdx[0][i]=chessIndex[i];
			else
				for(int i=0;i<64;i++)
					tempChessIdx[stepNum][i]=tempChessIdx[stepNum-1][i];*/
			if(stepNum==0?(chessIndex[x]>=7):(stepNum%2==0?(tempChessIdx[stepNum-1][x]>=7):(tempChessIdx[stepNum-1][x]>=1&&tempChessIdx[stepNum-1][x]<=6)))
			{
				Go=(stepNum%2==1);
				if(stepNum==0)
					isAccess(chessIndex,x);
				else
					isAccess(tempChessIdx[stepNum-1],x);
				for(int i=0;i<64;i++)
					tempAccess[stepNum][i]=access[i];
				for(int y=0;y<64;y++)
					if(tempAccess[stepNum][y])
					{
						//chessborad
						if(stepNum==0)
							for(int i=0;i<64;i++)
								tempChessIdx[0][i]=chessIndex[i];
						else
							for(int i=0;i<64;i++)
								tempChessIdx[stepNum][i]=tempChessIdx[stepNum-1][i];
						//chessborad ok

						//a possible step found
						setChess(tempChessIdx[stepNum],x,y,true);

						double t=-search(stepNum+1,-beta,-Math.max(alpha,tempScore));

					    if(t>tempScore)
						{
							/*if(t==1)
							{
								for(int i=0;i<64;i++)
								{
									System.out.printf("%d ",tempChessIdx[1][i]);
									if((i+1)%8==0)
										System.out.print("\n");
								}
							}*/
							tempScore=t;
							if(stepNum==0)
							{
								xAI=x;
								yAI=y;
							}
							if(t>=beta)
								return tempScore;
						}
					}

			}
		}
		return tempScore;
	}
	
}
