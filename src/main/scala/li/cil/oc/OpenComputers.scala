package li.cil.oc

import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.FMLInterModComms.IMCEvent
import cpw.mods.fml.common.event._
import cpw.mods.fml.common.network.FMLEventChannel
import cpw.mods.fml.common.{Mod, SidedProxy}
import li.cil.oc.common.template.AssemblerTemplates
import li.cil.oc.common.{Proxy, ToolDurabilityProviders}
import li.cil.oc.server.CommandHandler
import net.minecraftforge.common.util.Constants.NBT
import org.apache.logging.log4j.LogManager

import scala.collection.convert.WrapAsScala._

@Mod(modid = OpenComputers.ID, name = OpenComputers.Name,
  version = OpenComputers.Version,
  modLanguage = "scala", useMetadata = true)
object OpenComputers {
  final val ID = "OpenComputers"

  final val Name = "OpenComputers"

  final val Version = "@VERSION@"

  var log = LogManager.getLogger("OpenComputers")

  @SidedProxy(clientSide = "li.cil.oc.client.Proxy", serverSide = "li.cil.oc.server.Proxy")
  var proxy: Proxy = null

  var channel: FMLEventChannel = _

  @EventHandler
  def preInit(e: FMLPreInitializationEvent) {
    log = e.getModLog
    proxy.preInit(e)
  }

  @EventHandler
  def init(e: FMLInitializationEvent) = proxy.init(e)

  @EventHandler
  def postInit(e: FMLPostInitializationEvent) = proxy.postInit(e)

  @EventHandler
  def missingMappings(e: FMLMissingMappingsEvent) = proxy.missingMappings(e)

  @EventHandler
  def serverStart(e: FMLServerStartingEvent) = CommandHandler.register(e)

  @EventHandler
  def imc(e: IMCEvent) = {
    for (message <- e.getMessages) {
      if (message.key == "registerAssemblerTemplate" && message.isNBTMessage) {
        if (message.getNBTValue.hasKey("name", NBT.TAG_STRING))
          log.info(s"Registering new assembler template '${message.getNBTValue.getString("name")}' from mod ${message.getSender}.")
        else
          log.info(s"Registering new, unnamed assembler template from mod ${message.getSender}.")
        AssemblerTemplates.add(message.getNBTValue)
      }
      else if (message.key == "registerToolDurabilityProvider" && message.isStringMessage) {
        log.info(s"Registering new tool durability provider '${message.getStringValue}' from mod ${message.getSender}.")
        ToolDurabilityProviders.add(message.getStringValue)
      }
    }
  }
}
