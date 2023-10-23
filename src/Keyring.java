package xy.sys;

import java.awt.*;
import java.awt.List;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.io.*;
import java.util.Calendar;
import xy.lib.*;
import xy.txt.*;

class Keyring extends Frame implements WindowListener, ActionListener, TextListener, ItemListener {

  public static void main(String[] args) {
    new Keyring(args.length > 0 ? args[0] : "keyring.dat",
      args.length > 1 ? Integer.parseInt(args[1]) : 30);
  }

  private Keyring(String name, int keep) {
    super("Keyring");
    setLayout(deck = new CardLayout());
    Panel panel, line;
    GridBagLayout layout = new GridBagLayout();
    GridBagConstraints left = new GridBagConstraints(), right = new GridBagConstraints();
    right.gridwidth = GridBagConstraints.REMAINDER;
    right.weightx = 1;
    right.fill = GridBagConstraints.HORIZONTAL;

    add("login", panel = new Panel(layout));
    left.insets = new Insets(10, 10, 0, 0);
    panel.add(new Label("Master Password:"), left);
    right.insets = new Insets(10, 5, 0, 10);
    panel.add(password = new TextField(), right);
    right.insets = new Insets(20, 10, 10, 10);
    panel.add(loginMess = new Label("", Label.CENTER), right);

    add("keyring", panel = new Panel(layout));
    panel.add(new Label("Search:"), left);
    if ((alive = keep) > 0) {
      right.insets = new Insets(10, 5, 0, 0);
      right.gridwidth = 1;
      panel.add(pattern = new TextField(), right);
      right.gridwidth = GridBagConstraints.REMAINDER;
      right.insets = new Insets(10, 5, 0, 10);
      right.weightx = 0;
      panel.add(clock = new Label("" + keep, Label.RIGHT), right);
      right.weightx = 1;
    } else {
      right.insets = new Insets(10, 5, 0, 10);
      panel.add(pattern = new TextField(), right);
    }
    right.insets = new Insets(10, 10, 0, 10);
    panel.add(match = new List(), right);
    right.insets = new Insets(10, 10, 10, 10);
    panel.add(line = new Panel(layout), right);
    left.insets = new Insets(0, 0, 0, 0);
    line.add(show = new Button("Show"), left);
    left.insets = new Insets(0, 5, 0, 0);
    line.add(delete = new Button("Delete"), left);
    line.add(update = new Button("Update"), left);
    line.add(manual = new Button("Manual"), left);
    line.add(merge = new Button("Merge"), left);
    line.add(master = new Button("Master"), left);

    add("show", panel = new Panel(layout));
    left.insets = new Insets(10, 10, 0, 0);
    left.anchor = GridBagConstraints.EAST;
    panel.add(new Label("Account:"), left);
    right.insets = new Insets(10, 5, 0, 10);
    panel.add(showAccount = new TextField(), right);
    left.insets = new Insets(5, 10, 0, 0);
    panel.add(oldDate = new Label(), left);
    right.insets = new Insets(5, 5, 0, 10);
    panel.add(oldPass = new TextField(), right);
    panel.add(newDate = new Label(), left);
    left.anchor = GridBagConstraints.CENTER;
    panel.add(newPass = new TextField(), right);
    right.insets = new Insets(10, 10, 10, 10);
    right.fill = GridBagConstraints.NONE;
    panel.add(dismiss = new Button("Dismiss"), right);
    right.fill = GridBagConstraints.HORIZONTAL;

    add("update", panel = new Panel(layout));
    left.insets = new Insets(10, 10, 10, 0);
    panel.add(new Label("Account:"), left);
    right.insets = new Insets(10, 5, 10, 10);
    panel.add(updateAccount = new TextField(), right);
    right.insets = new Insets(0, 10, 10, 10);
    right.weightx = 0;
    panel.add(line = new Panel(layout), right);
    right.fill = GridBagConstraints.NONE;
    left.insets = new Insets(0, 0, 0, 0);
    line.add(new Label("Length:"), left);
    left.weightx = 1;
    left.fill = GridBagConstraints.HORIZONTAL;
    line.add(length = new TextField("32", 0), left);
    left.weightx = 0;
    left.fill = GridBagConstraints.NONE;
    left.insets = new Insets(0, 5, 0, 0);
    line.add(lower = new Checkbox("Lower", true), left);
    line.add(upper = new Checkbox("Upper", true), left);
    line.add(digit = new Checkbox("Digit", true), left);
    right.insets = new Insets(0, 5, 0, 0);
    line.add(symbol = new Checkbox("Symbol"), right);
    left.insets = new Insets(5, 5, 0, 0);
    left.gridwidth = 5;
    line.add(new Label(""), left);
    left.gridwidth = 1;
    line.add(allow = new Button("Allow"), left);
    right.insets = new Insets(10, 10, 10, 10);
    panel.add(line = new Panel(layout), right);
    left.insets = new Insets(0, 0, 0, 0);
    line.add(create = new Button("Create"), left);
    left.insets = new Insets(0, 10, 0, 0);
    line.add(updateCancel = new Button("Cancel"), left);

    add("symbol", panel = new Panel(layout));
    panel.add(line = new Panel(layout), right);
    left.insets = new Insets(0, 0, 0, 5);
    left.anchor = GridBagConstraints.WEST;
    right.insets = new Insets(0, 0, 0, 0);
    right.anchor = GridBagConstraints.WEST;
    for (int i = 33, j = 0; i < 127; i++) {
      if (i >= '0' && i <= '9' || i >= 'A' && i <= 'Z' || i >= 'a' && i <= 'z') continue;
      line.add(pick[j] = new Checkbox(" " + (char)i, true), j % 8 < 7 ? left : right);
      j++;
    }
    left.anchor = GridBagConstraints.CENTER;
    right.anchor = GridBagConstraints.CENTER;
    right.insets = new Insets(0, 10, 10, 10);
    panel.add(line = new Panel(layout), right);
    left.insets = new Insets(0, 0, 0, 0);
    line.add(full = new Button("All"), left);
    left.insets = new Insets(0, 10, 0, 0);
    line.add(none = new Button("None"), left);
    line.add(done = new Button("Done"), left);

    add("manual", panel = new Panel(layout));
    left.insets = new Insets(10, 10, 0, 0);
    left.anchor = GridBagConstraints.EAST;
    panel.add(new Label("Account:"), left);
    right.insets = new Insets(10, 5, 0, 10);
    right.weightx = 1;
    right.fill = GridBagConstraints.HORIZONTAL;
    panel.add(manualAccount = new TextField(), right);
    left.insets = new Insets(5, 10, 0, 0);
    panel.add(new Label("Password:"), left);
    left.anchor = GridBagConstraints.CENTER;
    right.insets = new Insets(5, 5, 0, 10);
    panel.add(manualPass = new TextField(), right);
    right.insets = new Insets(10, 10, 10, 10);
    panel.add(line = new Panel(layout), right);
    left.insets = new Insets(0, 0, 0, 0);
    line.add(showPass = new Checkbox("Show"), left);
    left.insets = new Insets(0, 10, 0, 0);
    line.add(save = new Button("Save"), left);
    line.add(manualCancel = new Button("Cancel"), left);

    add("merge", panel = new Panel(layout));
    left.insets = new Insets(10, 10, 0, 0);
    left.anchor = GridBagConstraints.EAST;
    panel.add(new Label("Filename:"), left);
    right.insets = new Insets(10, 5, 0, 10);
    panel.add(filename = new TextField(), right);
    left.insets = new Insets(5, 10, 0, 0);
    panel.add(new Label("Password:"), left);
    left.anchor = GridBagConstraints.CENTER;
    right.insets = new Insets(5, 5, 0, 10);
    panel.add(mergePass = new TextField(), right);
    right.insets = new Insets(10, 10, 0, 10);
    panel.add(mergeMess = new Label("", Label.CENTER), right);
    right.insets = new Insets(10, 10, 10, 10);
    panel.add(line = new Panel(layout), right);
    left.insets = new Insets(0, 0, 0, 0);
    line.add(combine = new Button("Merge"), left);
    left.insets = new Insets(0, 10, 0, 0);
    line.add(mergeCancel = new Button("Cancel"), left);

    add("master", panel = new Panel(layout));
    left.insets = new Insets(10, 10, 0, 0);
    left.anchor = GridBagConstraints.EAST;
    panel.add(new Label("Old Password:"), left);
    right.insets = new Insets(10, 5, 0, 10);
    panel.add(oldMaster = new TextField(), right);
    left.insets = new Insets(5, 10, 0, 0);
    panel.add(new Label("New Password:"), left);
    right.insets = new Insets(5, 5, 0, 10);
    panel.add(newMaster = new TextField(), right);
    panel.add(new Label("Confirmation:"), left);
    left.anchor = GridBagConstraints.CENTER;
    panel.add(confirm = new TextField(), right);
    right.insets = new Insets(10, 10, 10, 10);
    panel.add(line = new Panel(layout), right);
    left.insets = new Insets(0, 0, 0, 0);
    line.add(change = new Button("Change"), left);
    left.insets = new Insets(0, 10, 0, 0);
    line.add(masterCancel = new Button("Cancel"), left);

    password.setEchoChar('*');
    showAccount.setEditable(false);
    oldPass.setEditable(false);
    newPass.setEditable(false);
    allow.setEnabled(false);
    manualPass.setEchoChar('*');
    mergePass.setEchoChar('*');
    combine.setEnabled(false);
    oldMaster.setEchoChar('*');
    newMaster.setEchoChar('*');
    confirm.setEchoChar('*');
    change.setEnabled(false);

    pack();
    setResizable(false);
    Dimension window = getSize(), screen = Toolkit.getDefaultToolkit().getScreenSize();
    setLocation(screen.width - window.width >> 1, screen.height - window.height >> 1);
    setVisible(true);
    datafile = name;

    addWindowListener(this);
    password.addActionListener(this);
    password.addTextListener(this);
    pattern.addTextListener(this);
    match.addItemListener(this);
    show.addActionListener(this);
    delete.addActionListener(this);
    update.addActionListener(this);
    manual.addActionListener(this);
    merge.addActionListener(this);
    master.addActionListener(this);
    dismiss.addActionListener(this);
    updateAccount.addTextListener(this);
    length.addTextListener(this);
    lower.addItemListener(this);
    upper.addItemListener(this);
    digit.addItemListener(this);
    symbol.addItemListener(this);
    allow.addActionListener(this);
    create.addActionListener(this);
    updateCancel.addActionListener(this);
    full.addActionListener(this);
    none.addActionListener(this);
    done.addActionListener(this);
    manualAccount.addTextListener(this);
    manualPass.addTextListener(this);
    showPass.addItemListener(this);
    save.addActionListener(this);
    manualCancel.addActionListener(this);
    filename.addTextListener(this);
    mergePass.addTextListener(this);
    combine.addActionListener(this);
    mergeCancel.addActionListener(this);
    oldMaster.addTextListener(this);
    newMaster.addTextListener(this);
    confirm.addTextListener(this);
    change.addActionListener(this);
    masterCancel.addActionListener(this);
  }

  public void windowOpened(WindowEvent event) {}
  public void windowClosed(WindowEvent event) {}
  public void windowIconified(WindowEvent event) {}
  public void windowDeiconified(WindowEvent event) {}
  public void windowActivated(WindowEvent event) {}
  public void windowDeactivated(WindowEvent event) {}

  public void windowClosing(WindowEvent event) {
    clipboard.setContents(new StringSelection(""), null);
    if (modify) try {
      byte[] array = new byte [22];
      random.init();
      for (int i = 0; i < 8; array[i++] = (byte)('!' + random.next(94)));
      random.init(new String(array, 0, 8) + secret);
      for (int i = 8; i < 16; array[i++] = (byte)random.next(256));
      int size = 0;
      for (int i = 0; i < keys.length; size += keys[i++].getSize());
      Buffer buffer = new Buffer(array);
      buffer.putShort(keys.length, 16).putInteger(size);
      for (int i = 16; i < 22; array[i++] ^= random.next(256));
      FileOutputStream fos = new FileOutputStream(datafile);
      fos.write(array);
      buffer.setBuffer(array = new byte [size]);
      for (int i = 0; i < keys.length; keys[i++].write(buffer));
      for (int i = 0; i < size; array[i++] ^= random.next(256));
      fos.write(array);
      fos.close();
    } catch (Exception error) {
      System.out.println("Error: " + error);
    }
    System.exit(0);
  }

  public void actionPerformed(ActionEvent event) {
    Object source = event.getSource();
    if (timer != null && source != timer) timer.reset(alive);
    if (source == password) {
      if ((secret = password.getText().trim()).equals("")) return;
      if (new File(datafile).exists()) try {
        FileInputStream fis = new FileInputStream(datafile);
        byte[] array = new byte [22];
        fis.read(array);
        random.init(new String(array, 0, 8) + secret);
        for (int i = 8; i < 16; i++) {
          if (random.next(256) != (array[i] & 255)) {
            loginMess.setText("Incorrect password, try again.");
            password.selectAll();
            return;
          }
        }
        for (int i = 16; i < 22; array[i++] ^= random.next(256));
        Buffer buffer = new Buffer(array);
        keys = new Key [buffer.getShort(16)];
        buffer.setBuffer(array = new byte [buffer.getInteger()]);
        fis.read(array);
        fis.close();
        for (int i = 0; i < array.length; array[i++] ^= random.next(256));
        for (int i = 0; i < keys.length; keys[i++] = new Key(buffer));
        filterKey();
        merge.setEnabled(true);
      } catch (Exception error) {
        System.out.println("Error: " + error);
        System.exit(0);
      } else {
        show.setEnabled(false);
        delete.setEnabled(false);
        merge.setEnabled(false);
      }
      deck.show(this, "keyring");
      pattern.requestFocus();
      if (alive < 1) return;
      (timer = new Timer(alive)).addActionListener(this);
      timer.start();
    } else if (source == show) {
      Key key = getSelectedKey();
      showAccount.setText(key.name);
      oldDate.setText(key.oldDate > 0 ? key.getDate(key.oldDate) : "");
      oldPass.setText(key.oldDate > 0 ? key.oldPass : "");
      newDate.setText(key.getDate(key.newDate));
      newPass.setText(key.newPass);
      deck.show(this, "show");
    } else if (source == delete) {
      String select = match.getSelectedItem();
      Key[] tmp = new Key [keys.length - 1];
      for (int i = 0, j = 0; i < keys.length; i++)
        if (!keys[i].name.equals(select)) tmp[j++] = keys[i];
      keys = tmp;
      pattern.setText("");
      filterKey();
      modify = true;
    } else if (source == update) {
      String select = match.getSelectedItem();
      updateAccount.setText(select == null ? "" : select);
      checkCreate();
      deck.show(this, "update");
    } else if (source == manual) {
      String select = match.getSelectedItem();
      manualAccount.setText(select == null ? "" : select);
      manualPass.setText("");
      save.setEnabled(false);
      deck.show(this, "manual");
    } else if (source == merge) deck.show(this, "merge");
    else if (source == master) {
      if ((event.getModifiers() & 0xf) == (event.SHIFT_MASK | event.CTRL_MASK))
        for (int i = 0; i < keys.length; keys[i++].print());
      else deck.show(this, "master");
    } else if (source == dismiss || source == updateCancel || source == manualCancel ||
      source == mergeCancel || source == masterCancel) deck.show(this, "keyring");
    else if (source == allow) deck.show(this, "symbol");
    else if (source == full || source == none)
      for (int i = 0; i < 32; pick[i++].setState(source == full));
    else if (source == done) {
      boolean has = false;
      for (int i = 0; i < 32; i++) if (pick[i].getState()) has = true;
      symbol.setState(has);
      allow.setEnabled(has);
      checkCreate();
      deck.show(this, "update");
    } else if (source == create || source == save) {
      String name, pass;
      if (source == create) {
        name = updateAccount.getText().trim();
        byte[] array = new byte [Integer.parseInt(length.getText().trim())], sym = new byte [94];
        int scnt = 0;
        if (symbol.getState()) for (int i = 0; i < 32; i++)
          if (pick[i].getState()) sym[scnt++] = (byte)pick[i].getLabel().charAt(1);
        int top = scnt;
        if (lower.getState()) for (int i = 0; i < 26; sym[top++] = (byte)('a' + i++));
        if (upper.getState()) for (int i = 0; i < 26; sym[top++] = (byte)('A' + i++));
        if (digit.getState()) for (int i = 0; i < 10; sym[top++] = (byte)('0' + i++));
        random.init();
        if (scnt > 0) pickPlace(array, random, sym[(int)random.next(scnt)]);
        if (lower.getState()) pickPlace(array, random, (byte)('a' + random.next(26)));
        if (upper.getState()) pickPlace(array, random, (byte)('A' + random.next(26)));
        if (digit.getState()) pickPlace(array, random, (byte)('0' + random.next(10)));
        for (int i = 0; i < array.length; i++)
          if (array[i] == 0) array[i] = sym[(int)random.next(top)];
        pass = new String(array);
      } else {
        name = manualAccount.getText().trim();
        pass = manualPass.getText().trim();
      }
      if (keys != null) for (int i = 0; i < keys.length; i++) if (keys[i].name.equals(name)) {
        keys[i].update(pass);
        name = null;
        filterKey();
        break;
      }
      if (name != null) {
        Key[] tmp = new Key [keys == null ? 1 : keys.length + 1];
        if (keys == null) merge.setEnabled(true);
        else System.arraycopy(keys, 0, tmp, 0, keys.length);
        tmp[tmp.length - 1] = new Key(name, pass);
        keys = tmp;
        filterKey();
      }
      modify = true;
      deck.show(this, "keyring");
    } else if (source == combine) {
      String name = filename.getText().trim();
      if (new File(name).exists()) try {
        FileInputStream fis = new FileInputStream(name);
        byte[] array = new byte [22];
        fis.read(array);
        random.init(new String(array, 0, 8) + mergePass.getText().trim());
        for (int i = 8; i < 16; i++) {
          if (random.next(256) == (array[i] & 255)) continue;
          mergeMess.setText("Incorrect password, try again.");
          mergePass.selectAll();
          mergePass.requestFocus();
          return;
        }
        for (int i = 16; i < 22; array[i++] ^= random.next(256));
        Buffer buffer = new Buffer(array);
        Key[] other = new Key [buffer.getShort(16)];
        buffer.setBuffer(array = new byte [buffer.getInteger()]);
        fis.read(array);
        fis.close();
        for (int i = 0; i < array.length; array[i++] ^= random.next(256));
        int ncnt = 0;
        for (int i = 0; i < other.length; i++, ncnt++) {
          other[ncnt] = new Key(buffer);
          for (int j = 0; j < keys.length; j++) if (keys[j].name.equals(other[ncnt].name)) {
            if (keys[j].merge(other[ncnt--])) modify = true;
            break;
          }
        }
        if (ncnt > 0) {
          Key[] both = new Key [keys.length + ncnt];
          System.arraycopy(keys, 0, both, 0, keys.length);
          System.arraycopy(other, 0, both, keys.length, ncnt);
          keys = both;
          modify = true;
          filterKey();
        }
        filename.setText("");
        mergePass.setText("");
        combine.setEnabled(false);
        deck.show(this, "keyring");
      } catch (Exception error) {
        mergeMess.setText("Error: " + error);
      } else {
        mergeMess.setText("Data file not found, try again.");
        filename.selectAll();
        filename.requestFocus();
      }
    } else if (source == change) {
      secret = newMaster.getText().trim();
      oldMaster.setText("");
      newMaster.setText("");
      confirm.setText("");
      change.setEnabled(false);
      modify = true;
      deck.show(this, "keyring");
    } else if (source == timer) {
      int count = (int)event.getWhen();
      if (count > 0) clock.setText("" + count);
      else windowClosing(null);
    }
  }

  public void textValueChanged(TextEvent event) {
    if (timer != null) timer.reset(alive);
    Object source = event.getSource();
    if (source == password) loginMess.setText("");
    else if (source == pattern && keys != null) filterKey();
    else if (source == updateAccount || source == length) checkCreate();
    else if (source == manualAccount || source == manualPass)
      save.setEnabled(!manualAccount.getText().trim().equals("") &&
        !manualPass.getText().trim().equals(""));
    else if (source == filename || source == mergePass) {
      combine.setEnabled(!filename.getText().trim().equals("") &&
        !mergePass.getText().trim().equals(""));
      mergeMess.setText("");
    } else if (source == oldMaster || source == newMaster || source == confirm)
      change.setEnabled(oldMaster.getText().trim().equals(secret) &&
        !newMaster.getText().trim().equals("") &&
        confirm.getText().trim().equals(newMaster.getText().trim()));
  }

  public void itemStateChanged(ItemEvent event) {
    if (timer != null) timer.reset(alive);
    Object source = event.getSource();
    if (source == match) {
      Key key = getSelectedKey();
      clipboard.setContents(new StringSelection(key.newPass), null);
      show.setEnabled(true);
      delete.setEnabled(keys.length > 1);
    } else if (source == lower || source == upper || source == digit || source == symbol) {
      allow.setEnabled(symbol.getState());
      checkCreate();
    } else if (source == showPass) manualPass.setEchoChar(showPass.getState() ? 0 : '*');
  }

  private void filterKey() {
    match.removeAll();
    String[] token = Text.tokenize(pattern.getText().toUpperCase(), 9);
    for (int i = 0; i < keys.length; i++) if (keys[i].match(token)) match.add(keys[i].name);
    if (match.getItemCount() == 1) {
      match.select(0);
      itemStateChanged(new ItemEvent(match,
        ItemEvent.ITEM_STATE_CHANGED, null, ItemEvent.SELECTED));
    } else {
      clipboard.setContents(new StringSelection(""), null);
      show.setEnabled(false);
      delete.setEnabled(false);
    }
  }

  private Key getSelectedKey() {
    String select = match.getSelectedItem();
    for (int i = 0; i < keys.length; i++) if (keys[i].name.equals(select)) return keys[i];
    return null;
  }

  private void checkCreate() {
    int len = 0;
    try {
      len = Integer.parseInt(length.getText().trim());
    } catch (Exception error) {}
    create.setEnabled(!updateAccount.getText().trim().equals("") && len > 0 &&
      (lower.getState() || upper.getState() || digit.getState() || symbol.getState()));
  }

  private void pickPlace(byte[] array, Random random, byte sym) {
    int i = (int)random.next(array.length);
    while (array[i] > 0) i = (int)random.next(array.length);
    array[i] = sym;
  }

  private int alive;
  private CardLayout deck;
  private TextField password, pattern, showAccount, oldPass, newPass, updateAccount, length;
  private TextField manualAccount, manualPass, filename, mergePass, oldMaster, newMaster, confirm;
  private Label loginMess, clock, oldDate, newDate, mergeMess;
  private List match;
  private Button show, delete, update, manual, merge, master, dismiss, allow, create, updateCancel;
  private Button full, none, done, save, manualCancel, combine, mergeCancel, change, masterCancel;
  private Checkbox lower, upper, digit, symbol, pick[] = new Checkbox [32], showPass;
  private String datafile, secret;
  private Random random = new Random();
  private Key[] keys;
  private Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
  private boolean modify;
  private Timer timer;

  private static class Key {
    Key(Buffer buffer) throws Exception {
      name = buffer.getString(buffer.getByte() & 255);
      if ((oldDate = buffer.getShort()) > 0) oldPass = buffer.getString(buffer.getByte() & 255);
      newDate = buffer.getShort();
      newPass = buffer.getString(buffer.getByte() & 255);
    }
    Key(String acct, String pass) {
      name = acct;
      oldDate = 0;
      newDate = today();
      newPass = pass;
    }
    void update(String pass) {
      oldDate = newDate;
      oldPass = newPass;
      newDate = today();
      newPass = pass;
    }
    int today() {
      Calendar calendar = Calendar.getInstance();
      int now = (int)(calendar.getTimeInMillis() / 3600000);
      calendar.set(2010, 0, 1);
      return (int)(now - calendar.getTimeInMillis() / 3600000 + 12) / 24;
    }
    boolean match(String[] token) {
      String upper = name.toUpperCase();
      for (int i = 0; i < token.length && !token[i].equals(""); i++)
        if (upper.indexOf(token[i]) < 0) return false;
      return true;
    }
    String getDate(int date) {
      Calendar calendar = Calendar.getInstance();
      calendar.set(2010, 0, 1);
      calendar.add(Calendar.DATE, date);
      return calendar.get(Calendar.MONTH) + 1 + "/" +
        calendar.get(Calendar.DATE) + "/" + calendar.get(Calendar.YEAR) + ":";
    }
    boolean merge(Key one) {
      if (one.newDate <= oldDate) return false;
      if (one.newDate < newDate) {
        oldDate = one.newDate;
        oldPass = one.newPass;
      } else if (one.newDate == newDate) {
        if (one.oldDate <= oldDate) return false;
        oldDate = one.oldDate;
        oldPass = one.oldPass;
      } else {
        if (one.oldDate > newDate) {
          oldDate = one.oldDate;
          oldPass = one.oldPass;
        } else {
          oldDate = newDate;
          oldPass = newPass;
        }
        newDate = one.newDate;
        newPass = one.newPass;
      }
      return true;
    }
    int getSize() {
      int len = name.length() + newPass.length() + 6;
      if (oldDate > 0) len += oldPass.length() + 1;
      return len;
    }
    void write(Buffer buffer) throws Exception {
      buffer.putByte(name.length()).putString(name, name.length());
      buffer.putShort(oldDate);
      if (oldDate > 0) buffer.putByte(oldPass.length()).putString(oldPass, oldPass.length());
      buffer.putShort(newDate).putByte(newPass.length()).putString(newPass, newPass.length());
    }
    void print() {
      System.out.println(name + "\t" + (oldDate > 0 ? getDate(oldDate) : "_") + "\t" +
        (oldDate > 0 ? oldPass : "_") + "\t" + getDate(newDate) + "\t" + newPass);
    }
    String name, oldPass, newPass;
    int oldDate, newDate;
  }

  private static class Timer extends Thread {
    Timer(int total) {
      count = total;
    }
    void reset(int total) {
      count = total;
    }
    void addActionListener(ActionListener ear) {
      listener = AWTEventMulticaster.add(listener, ear);
    }
    public void run() {
      for (count--;; count--) try {
        sleep(1000);
        if (listener != null) listener.actionPerformed(
          new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "", count, 0));
      } catch (Exception error) {}
    }
    int count;
    ActionListener listener;
  }
}
