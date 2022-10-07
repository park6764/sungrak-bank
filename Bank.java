import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Optional;

public class Bank {
    public static void main(String[] args) {
        ArrayList<PassBook> PB = new ArrayList();
        ArrayList<DealingRecord> DR = new ArrayList();
        Scanner scan = new Scanner(System.in);
        int index = 0;

        while(true){
            select();

            var user = Integer.parseInt(scan.nextLine());

            switch(user){
                case 1 : PB = PassBook.NewPassBook(scan, System.out, PB, index); index++; break;
                case 2 : DR = DealingRecord.addMoney(scan, System.out, PB, DR); break;
                case 3 : DR = DealingRecord.withdraw(scan, System.out, PB, DR); break;
                case 4 : DR = DealingRecord.send(scan, System.out, PB, DR); break;
                case 5 : System.out.println(DR.toString()); break; 
                case 0 : System.out.println("프로그램을 종료합니다."); return;
                default : System.out.println("0 ~ 6사이의 수를 입력하세요.");
            }
        }
    }

    static void select() {
        System.out.println("=======================");
        System.out.println("성락뱅크에 오신걸 환영합니다.");
        System.out.println("원하는 서비스를 선택하세요.");
        System.out.println("통장 개설은 1번");
        System.out.println("입금은 2번");
        System.out.println("출금은 3번");
        System.out.println("송금은 4번");
        System.out.println("거래내역 조회는 5번");
        System.out.println("서비스 종료는 0번");
        System.out.println("=======================");
        System.out.println(" ");
    }

    static ArrayList<PassBook> balance(ArrayList<PassBook> PB, ArrayList<DealingRecord> DR) {}
}

class PassBook{
    private String name; // 이름
    private String passBookNumber; // 계좌번호
    private Integer passWord; // 비밀번호
    private Integer birth; // 생년월일
    private Integer cashs; // 잔액

    PassBook(String name, String passBookNumber, Integer passWord,  Integer birth, Integer cashs){
        this.name = name;
        this.passBookNumber = passBookNumber;
        this.passWord = passWord;
        this.birth = birth;
        this.cashs = cashs;
    }

    public String getName(){ return name; }
    public String getPassBookNumber(){ return passBookNumber; }
    public Integer getPassWord(){ return passWord;}
    public Integer getBirth(){ return birth; }
    public Integer getCashs(){ return cashs; }

    public static ArrayList<PassBook> NewPassBook(Scanner scan, PrintStream out, ArrayList<PassBook> PB, int index){
        out.println("통장 개설을 선택하셨습니다.");
        out.println("이름을 입력해 주세요. (단, 통장 개설은 1인 1통장만 가능합니다.)");
        out.println("->");
        var name = scan.nextLine(); // 개설자 이름

        out.println("생년월일 6자리는 입력해주세요. (ex. 980914)");
        out.println("-> ");
        var birth = Integer.parseInt(scan.nextLine()); // 개설자의 생년월일

            if(IndexOfName(PB, name) == -1 && SixWord(birth)) { 
                out.println("새로운 통장 비밀번호 4자리를 입력하세요.");
                out.println("-> ");
                var passWord = Integer.parseInt(scan.nextLine()); // 개설 통장 비밀번호

                if(FourWord(passWord)){
                    out.println("통장 개설을 위해 최소금액 '10000원'을 입금하셔야 합니다.");
                    out.println("입금 하시겠습니까? (예 / 아니요)를 입력해주세요.");
                    out.println("-> ");
                    var answer = scan.nextLine();

                    if(answer.equals("예")){
                        out.println("입금하실 금액을 입력해주세요.");
                        out.println("-> ");
                        var cashs = Integer.parseInt(scan.nextLine());

                        if(cashs >= 10000){
                            String bankNumber = "8291"; // 은행 고유번호
                            String NPBN = Integer.toString(birth);
                            String idx = Integer.toString(index);
                            String PassBookNumber = (bankNumber + "-" + NPBN + "-" + idx);

                            PB.add(new PassBook(name, PassBookNumber, passWord, birth, cashs));
                            out.println("통장을 개설하였습니다.");
                            out.println(name + "님의 통장 계좌번호는 [" + PassBookNumber + "] 입니다.");
                            out.println(""); // 잔액보여주자.
                            out.println("감사합니다. 다음에 또 이용해주세요.");

                            } else {
                                out.println("입금하신 금액이 '10000'이 아닙니다.");
                                out.println("통장개설에 필요한 최소금액 '10000'을 입금해주세요.");
                            }
                        } else {
                            out.println("'아니요'를 선택하셨습니다. 통장개설이 취소되었습니다.");
                            out.println("감사합니다. 다음에 또 이용해주세요.");
                        }
                    } else {
                        out.println("입력하신 비밀번호가 숫자가 아니거나 4자리가 아닙니다."); // 키패드만 보여진다고 가정(문자입력 불가능.)
                        out.println("통장개설이 취소되었습니다.");
                        out.println("감사합니다. 다음에 또 이용해주세요."); // 약간 사가지 없지만.. 일단은 프로그램 종료 
                    }
                } else {
                    out.println("통장 개설은 1인 1통장만 가능합니다.");
                    out.println("감사합니다. 다음에 또 이용해주세요.");
                    }
            return PB;
    }

    public static Integer IndexOfName(ArrayList<PassBook> PB, String name){ 
        for(int i = 0; i < PB.size(); i++){
            if(PB.get(i).getName().equals(name)){
                return i;
            } else continue;
        } return -1;
    }

    public static boolean FourWord(Integer passWord){
        var length = (int)(Math.log10(passWord) + 1);
        if(length == 4){ 
            return true;
        } else return false;
    }

    public static boolean SixWord(Integer birth){
        var length = (int)(Math.log10(birth) + 1);
        if(length == 6){ 
            return true;
        } else return false;
    }
}

class DealingRecord {
    private Optional<PassBook> sender; // PaasBook -> PassBook
    private Optional<PassBook> receiver;
    private Integer money;

    DealingRecord(PassBook sender, PassBook receiver, Integer money) {
        this.sender = sender == null ? Optional.empty() : Optional.of(sender);
        this.receiver = receiver == null ? Optional.empty() : Optional.of(receiver);
        this.money = money;
    }
    
    public Optional<PassBook> getReceiver() { return receiver; }
    public Optional<PassBook> getSender() { return sender; }
    public Integer getMoney() { return money; }

    // 입금하는 사람은 본인으로 가정.
    public static ArrayList<DealingRecord> addMoney(Scanner scan, PrintStream out, ArrayList<PassBook> PB, ArrayList<DealingRecord> DR) {
        out.println("입금을 선택하셨습니다.");

        out.println("입금할 계좌번호를 입력하세요.");
        out.println("-> ");
        var bookNumber = scan.nextLine();

        if(bookIndexOfNumber(PB, bookNumber) > -1) {
            out.println("입금할 금액을 입력하세요.");
            out.println("-> "); // 숫자만 누른다고 가정.(키패드를 보여준다고 가정.)
            var money = Integer.parseInt(scan.nextLine());

            out.println("입금하시는 금액이" + money + " 가 맞습니까?");
            out.println("예 / 아니요");
            out.println("-> ");
            var choi = scan.nextLine();
            
            if(choi.equals("예")) {
                var i = bookIndexOfNumber(PB, bookNumber);
                DR.add(new DealingRecord(null, PB.get(i), money)); // (누가, 어느 계좌로, 얼마를 입금하였다.)
                out.println("[" + money + "](이)가 입금되었습니다.");
                out.println("거래 후 잔액은 [" + (PB.get(i).getCashs() + money) + "]원 입니다."); // 거래 후 잔액을 보여줘야한다.
                out.println("감사합니다. 다음에 또 이용해주세요.");
            } else {
                    out.println("입금이 취소되었습니다.");
                    out.println("감사합니다. 다음에 또 이용해주세요.");
                }
        } else {
                out.println("없는 계좌번호 입니다.");
                out.println("감사합니다. 다음에 또 이용해주세요.");
            }
        
        return DR;
    }

    public static Integer bookIndexOfNumber(ArrayList<PassBook> PB, String bookNumber){ 
        for(int i = 0; i < PB.size(); i++){
            if(PB.get(i).getPassBookNumber().equals(bookNumber)) {
                return i;
            } else continue;
        } return -1;
    }
// 본인만 현금을 인출한다고 가정.
    public static ArrayList<DealingRecord> withdraw(Scanner scan, PrintStream out, ArrayList<PassBook> PB, ArrayList<DealingRecord> DR){
        out.println("출금하기를 선택하셨습니다.");

        out.println("출금할 계좌번호를 입력해주세요.");
        out.println("-> ");
        var bookNumber = scan.nextLine();

        if(bookIndexOfNumber(PB, bookNumber) > -1) {
            out.println("통장 비밀번호를 입력해주세요.");
            out.println("-> ");
            var PassWord = Integer.parseInt(scan.nextLine());

            if(CheckPassWord(bookNumber, PassWord, PB)) {
                out.println("출금하실 금액을 입력해주세요.");
                out.println("-> ");
                var money = Integer.parseInt(scan.nextLine());

                if(CheckCash(bookNumber, money, PB)) {
                    out.println("출금하시는 금액이 [" + money + "]원 이 맞습니까?");
                    out.println("예 / 아니요");
                    out.println("-> ");
                    var answer = scan.nextLine();

                    if(answer.equals("예")){
                        out.println("출금 중... 잠시만 기다려 주세요.");
                        var i = bookIndexOfNumber(PB, bookNumber);
                        DR.add(new DealingRecord(PB.get(i), null, money)); // 출금내역 저장 (누가, 어느 계좌에서, 얼마를 출금하였다.)
                        out.println("출금이 완료되었습니다.");
                        out.println("거래 후 잔액은 [" + ((int)PB.get(i).getCashs() - money) + "]원 입니다."); // 거래 후 잔액을 보여줘야한다.
                        out.println("감사합니다. 다음에 또 이용해주세요.");
                    } else {
                        out.println("출금이 취소되었습니다.");
                        out.println("감사합니다. 다음에 또 이용해주세요.");
                    }
                } else {
                    out.println("잔액이 부족합니다.");
                    out.println("감사합니다. 다음에 또 이용해주세요.");
                }
            } else{
                out.println("통장 비밀번호가 다릅니다.");
                out.println("감사합니다. 다음에 또 이용해주세요.");
            }
        } else {
            out.println("없는 계좌번호입니다.");
            out.println("감사합니다. 다음에 또 이용해주세요.");
        }
        return DR;
    }

    public static boolean CheckPassWord(String bookNumber, Integer PassWord, ArrayList<PassBook> PB) {
        for(var book : PB) {
            if(book.getPassWord().equals(PassWord) && book.getPassBookNumber().equals(bookNumber)) { // 계좌번호와 비밀번호가 일치하는지 확인
                return true;
            } else continue;
        }
        return false;
    }

    public static boolean CheckCash(String withdrawalNumber, int withdrawalCash, ArrayList<PassBook> PB){ // 출금이 가능한지(출금액 < 잔액)
        for(var book : PB) {
            int cash = book.getCashs();
            if(book.getPassBookNumber().equals(withdrawalNumber) && (cash >= withdrawalCash)) { // 해당 계좌의 잔액이 > 출금액 인가?
                return true;
            } else continue;
        }
        return false;
    }

    // send :: (Scanner, PrintStream, ArrayList<PassBook>, ArrayList<DealingRecord>) -> ArrayList<DealingRecord>
    public static ArrayList<DealingRecord> send(Scanner scan, PrintStream out, ArrayList<PassBook> PB, ArrayList<DealingRecord> DR) {
        out.println("송금하기를 선택하셨습니다.");

        out.println("본인 계좌번호를 입력해주세요.");
        out.println("-> ");
        var fromNumber = scan.nextLine();

        out.println("받으시는 분의 계좌번호를 입력해주세요.");
        out.println("->");
        var toNumber = scan.nextLine();

        if(bookIndexOfNumber(PB, fromNumber) > -1 && bookIndexOfNumber(PB, toNumber) > -1) {
            out.println("통장 비밀번호를 입력해주세요.");
            out.println("-> ");
            var fromPassWord = Integer.parseInt(scan.nextLine());

            if(CheckPassWord(fromNumber, fromPassWord, PB)) {
                out.println("송금하실 금액을 입력해주세요.");
                out.println("-> ");
                var money = Integer.parseInt(scan.nextLine());

                if(CheckCash(fromNumber, money, PB)) {
                    out.println("송금하시는 금액이 [" + money + "]원 이 맞습니까?");
                    out.println("예 / 아니요");
                    out.println("-> ");
                    var answer = scan.nextLine();

                    if(answer.equals("예")){
                        out.println("송금 중... 잠시만 기다려 주세요.");
                        var i = bookIndexOfNumber(PB, fromNumber);
                        var j = bookIndexOfNumber(PB, toNumber);
                        DR.add(new DealingRecord(PB.get(i), PB.get(j), money)); // 출금내역 저장 (누가, 어느 계좌에서, 어느 계좌로, 얼마를 출금하였다.)
                        out.println("송금이 완료되었습니다.");
                        out.println("거래 후 잔액은 [" + (PB.get(i).getCashs() - money) + "]원 입니다."); // 거래 후 잔액을 보여줘야한다.
                        out.println("감사합니다. 다음에 또 이용해주세요.");
                    } else {
                        out.println("송금이 취소되었습니다.");
                        out.println("감사합니다. 다음에 또 이용해주세요.");
                    }
                } else {
                    out.println("잔액이 부족합니다.");
                    out.println("감사합니다. 다음에 또 이용해주세요.");
                }
            } else {
                out.println("통장 비밀번호가 다릅니다.");
                out.println("감사합니다. 다음에 또 이용해주세요.");
            }
        } else {
            out.println("없는 계좌번호입니다.");
            out.println("감사합니다. 다음에 또 이용해주세요.");
        }
        return DR;
    }

    @Override 
    public String toString(){
        if (sender.isPresent()) {
            if (receiver.isPresent()) {
                return sender.get().getPassBookNumber() + " -> " + receiver.get().getPassBookNumber() + "(" + money + ")";
                // 계좌번호 -> 계좌번호 (20000)
            }
            else {
                return sender.get().getPassBookNumber() + "에서 " + money + "원 출금";
            }
        } else {
            if (receiver.isPresent()) {
                return receiver.get().getPassBookNumber() + "로 " + money + "원 입금";
            }
            else {
                return "오류: 송금 계좌와 수신 계좌가 모두 없음";
            }
        }
    }
}
