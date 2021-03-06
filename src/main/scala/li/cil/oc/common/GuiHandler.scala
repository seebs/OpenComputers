package li.cil.oc.common

import cpw.mods.fml.common.network.IGuiHandler
import li.cil.oc.common.init.Items
import li.cil.oc.common.inventory.{DatabaseInventory, ServerInventory}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World

abstract class GuiHandler extends IGuiHandler {
  override def getServerGuiElement(id: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int) =
    world.getTileEntity(x, y, z) match {
      case adapter: tileentity.Adapter if id == GuiType.Adapter.id =>
        new container.Adapter(player.inventory, adapter)
      case assembler: tileentity.Assembler if id == GuiType.Assembler.id =>
        new container.Assembler(player.inventory, assembler)
      case charger: tileentity.Charger if id == GuiType.Charger.id =>
        new container.Charger(player.inventory, charger)
      case computer: tileentity.Case if id == GuiType.Case.id =>
        new container.Case(player.inventory, computer)
      case disassembler: tileentity.Disassembler if id == GuiType.Disassembler.id =>
        new container.Disassembler(player.inventory, disassembler)
      case drive: tileentity.DiskDrive if id == GuiType.DiskDrive.id =>
        new container.DiskDrive(player.inventory, drive)
      case proxy: tileentity.RobotProxy if id == GuiType.Robot.id =>
        new container.Robot(player.inventory, proxy.robot)
      case rack: tileentity.ServerRack if id == GuiType.Rack.id =>
        new container.ServerRack(player.inventory, rack)
      case switch: tileentity.Switch if id == GuiType.Switch.id =>
        new container.Switch(player.inventory, switch)
      case _ => Items.multi.subItem(player.getCurrentEquippedItem) match {
        case Some(database: item.UpgradeDatabase) if id == GuiType.Database.id =>
          new container.Database(player.inventory, new DatabaseInventory {
            override def tier = database.tier

            override def container = player.getCurrentEquippedItem

            override def isUseableByPlayer(player: EntityPlayer) = player == player
          })
        case Some(server: item.Server) if id == GuiType.Server.id =>
          new container.Server(player.inventory, new ServerInventory {
            override def tier = server.tier

            override def container = player.getCurrentEquippedItem

            override def isUseableByPlayer(player: EntityPlayer) = player == player
          })
        case _ => null
      }
    }
}