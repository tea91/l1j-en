/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */

package l1j.server.server.serverpackets;

import l1j.server.packethandler.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_OwnCharPack extends ServerBasePacket {

	private static final String S_OWN_CHAR_PACK = "[S] S_OwnCharPack";
	private static final int STATUS_POISON = 1;
	private static final int STATUS_INVISIBLE = 2;
	private static final int STATUS_PC = 4;
	private static final int STATUS_FREEZE = 8;
	private static final int STATUS_BRAVE = 16;
	private static final int STATUS_ELFBRAVE = 32;
	private static final int STATUS_FASTMOVABLE = 64;
	private static final int STATUS_GHOST = 128;

	private byte[] _byte = null;

	public S_OwnCharPack(L1PcInstance pc) {
		buildPacket(pc);
	}

	private void buildPacket(L1PcInstance pc) {
		int status = STATUS_PC;
		int light = pc.hasSkillEffect(2) ? 14 : 0;

		if (pc.isInvisble() || pc.isGmInvis()) {
			status |= STATUS_INVISIBLE;
		}
		if (pc.isBrave()) {
			status |= STATUS_BRAVE;
			if (pc.isElf()) {
				status |= STATUS_ELFBRAVE;
			}
		}
		
		//need to be tested
		if (pc.isPoison()) { // status |= STATUS_POISON; //  
			status |= STATUS_POISON;
			} 
		//need to be tested
		if (pc.isFreeze()) { // status |= STATUS_FREEZE; //  
			status |= STATUS_FREEZE;
			} 
		
		if (pc.isWisPot())
		{
			// there's two bitflags for braves, trying to set first, but not second
			status |= STATUS_ELFBRAVE;
		}
		if (pc.isFastMovable()) {
			status |= STATUS_FASTMOVABLE;
		}
		if (pc.isGhost()) {
			status |= STATUS_GHOST;
		}

		writeC(Opcodes.S_OPCODE_CHARPACK);
		writeH(pc.getX());
		writeH(pc.getY());
		writeD(pc.getId());
		writeH(pc.getTempCharGfx());
		if (pc.isDead()) {
			writeC(pc.getStatus());
		} else {
			writeC(pc.getCurrentWeapon());
		}
		writeC(pc.getHeading());
		writeC(light); 
		writeC(pc.getMoveSpeed());
		writeD(pc.getExp());
		writeH(pc.getLawful());
		writeS(pc.getName());
		writeS(pc.getTitle());
		writeC(status);
		writeD(pc.getClanid());
		writeS(pc.getClanname()); 
		writeS(null); 
		writeC(0); 
		if (pc.isInParty()) 
		{
			writeC(100 * pc.getCurrentHp() / pc.getMaxHp());
		} else {
			writeC(0xFF);
		}
		writeC(0);
		writeC(0); 
		writeC(0); 
		writeC(0xFF);
		writeC(0xFF);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}

	public String getType() {
		return S_OWN_CHAR_PACK;
	}

}
