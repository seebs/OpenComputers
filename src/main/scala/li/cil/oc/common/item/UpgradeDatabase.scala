package li.cil.oc.common.item

import li.cil.oc.common.GuiType
import li.cil.oc.util.Rarity
import li.cil.oc.{OpenComputers, Settings}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World

class UpgradeDatabase(val parent: Delegator, val tier: Int) extends Delegate with ItemTier {
  override val unlocalizedName = super.unlocalizedName + tier

  override protected def tooltipName = Option(super.unlocalizedName)

  override protected def tooltipData = Seq(Settings.get.databaseEntriesPerTier(tier))

  override def rarity = Rarity.byTier(tier)

  override def onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer) = {
    if (!player.isSneaking) {
      player.openGui(OpenComputers, GuiType.Database.id, world, 0, 0, 0)
      player.swingItem()
    }
    else if (stack.hasTagCompound && stack.getTagCompound.hasKey(Settings.namespace + "items")) {
      stack.setTagCompound(null)
      player.swingItem()
    }
    stack
  }
}
