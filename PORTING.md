# NeoForge 1.21.1 Port

## 基线与目标

- 代码基线：VulpesStella/MrCrayfishGunMod `dev`，`18961e22`。
- 原环境：Minecraft 1.20.1、Forge 47.4.20、ForgeGradle 5.1、Java 17、Gradle 7.6.6。
- 目标：Minecraft 1.21.1、NeoForge、Java 21、官方 ModDevGradle 构建体系。
- 保留全部原始许可证、作者声明、资源与枪械核心行为。第三方移植仅用于逐模块对比，不替换代码基线。

## Completed

- [x] 修改前审计项目结构、构建脚本、依赖、注册、网络、资源和联动。
- [x] 确认本机现有 JDK 21：IntelliJ IDEA 的 JBR 21.0.6（仅本机验证使用，路径不写入可移植构建配置）。

## In Progress / TODO

- [x] Phase 1：迁移 settings/build/properties、Gradle distribution、NeoForge metadata、Mojmap、client/server/data runs；`gradlew help` 成功（Gradle 9.2.1 / ModDevGradle 2.0.146 / NeoForge 21.1.249）。Wrapper launcher 升级待执行 wrapper task。
- [ ] Phase 2：注册、生命周期、Event Bus 和基础 Java API；运行 compileJava。
- [ ] Phase 3：确认并接入 Framework 1.21.1 NeoForge；迁移数据加载和同步 API。
- [ ] Phase 4：ItemStack 数据组件、枪械、弹药、换弹、弹丸、伤害、配件、附魔、声音与配置。
- [ ] Phase 5：Payload 网络、客户端/服务端同步、登录数据与服务端校验。
- [ ] Phase 6：客户端渲染、动画、HUD、瞄具、模型、按键和 Screen。
- [ ] Phase 7：数据生成、配方、标签、战利品表、附魔和资源包格式。
- [ ] Phase 8：JEI / Configured 的 1.21.1 兼容。
- [ ] Phase 9：逐个评估其他可选联动，恢复前不作为核心运行依赖。
- [ ] 验证 clean、build、runData、runClient、测试世界与 runServer。

## 修改前审计

| 范围 | 原实现与迁移关注点 |
| --- | --- |
| Gradle | build.gradle 使用 ForgeGradle 5.1.+、MixinGradle 0.7.+、Parchment Librarian 1.+；fg.deobf 与 reobfJar 需替换；settings.gradle 配置旧 Forge 插件仓库。 |
| properties / wrapper | 1.20.1、Forge 47.4.20、CGM-Unofficial 1.4.20；Gradle 7.6.6 不适合作为 Java 21 构建基线。 |
| metadata | META-INF/mods.toml，cgm，GPL3 声明，Framework 必需，Controllable/Backpacked/JEI 可选。 |
| AT / AW | 未发现 Access Transformer 或 Access Widener；ReflectionUtil 和客户端反射使用旧 SRG 字段/方法名，需要审计。 |
| Mixin | cgm.mixins.json：5 个 common、6 个 client；MixinPlugin 探测旧 FrameworkForge 类，JAVA_8、旧 refmap 与注入目标需要更新。 |
| API 使用 | Forge client/event/common/fml/network/registries 等；不能只修改包名前缀。 |
| 注册 | init 下 DeferredRegister / RegistryObject 注册 item、block、entity、sound、particle、menu、recipe、effect、enchantment、block entity；1.21 附魔数据驱动。 |
| 网络 | FrameworkNetwork / PlayMessage（15 种消息），不是直接使用 SimpleChannel；Framework 登录数据用于同步 guns/custom_guns。 |
| 玩家数据 | Framework SyncedDataKey 同步 AIMING / RELOADING / SHOOTING；没有独立的核心 Forge Capability provider。 |
| Item 数据 | 49 处旧 ItemStack NBT 调用，含 AmmoCount、IgnoreAmmo、Gun、Custom、Attachments、Color、AdditionalDamage、模型属性；需要复制后写回的组件语义。 |
| Capability | 主要在 Traveler's Backpack、Curios 等第三方联动中；AmmoContext 保存扣弹后的容器回调。 |
| 服务端玩法 | ServerPlayHandler 校验开火/合成并扣弹；ReloadTracker 按 tick 换弹；ProjectileEntity 运动线段碰撞、爆头、暴击、衰减、流体减速；保留这些行为。 |
| 客户端 | ClientHandler、Shooting/Aiming/Reload/Recoil/GunRenderingHandler、屏幕、模型、粒子、声音、按键；审计 client-only 加载边界。 |
| DataGen / JSON | GunGen/RecipeGen/TagGen/LootTableGen，src/generated/resources；旧 recipes/loot_tables/tags/blocks 等目录、配方序列化及新注册表数据需要更新。 |
| 资源 | assets/cgm 模型、纹理、sounds、12 种语言；内置可选 PBR 资源包。 |

## 依赖清单与迁移顺序

| 原依赖 | 旧配置 | 计划 |
| --- | --- | --- |
| Framework | Curse 549225:7573740 | 核心必需；核实 NeoForge 1.21.1 artifact 与 API |
| JEI | 15.11.2.44 | 核心可构建后迁移 API |
| Configured | Curse 457570:5180900 | 验证 NeoForge 1.21.1 可选运行依赖 |
| Catalogue | Curse 459701:4766090 | 可选联动，暂不阻塞核心 |
| Curios | 5.14.1+1.20.1 | 随背包联动逐项恢复 |
| Backpacked | Curse 352835:7866682 | 保留 Quiverlink、多背包查弹及扣弹回调语义 |
| Simple Planes | Curse 388908:4893679 | 暂时隔离飞机联动及对应 Mixin |
| Recruits | Curse 523860:4846123 | 旧开发环境依赖；不作为核心必需 |
| Sophisticated Core / Backpacks | Curse 618298:4993651 / 422301:4993659 | 暂时隔离联动 |
| Traveler's Backpack | Curse 321117:8154660 | 暂时隔离联动 |
| L2 Library / Backpack | Curse 620203:5179254 / 620229:5107073 | 暂时隔离联动 |
| Controllable | Curse 317269:4598985 | 暂时隔离控制器联动 |
| CreativeCore / CMDCam | Curse 257814:5056178 / 251244:5019847 | 暂时隔离相机联动 |
| PlayerRevive | 运行时条件检测 | 暂时隔离倒地状态联动 |

## Temporarily Disabled Integrations

目前仍在 source set 中排除 SimplePlanes、CMDCam 和 Controllable 适配器；源码保留，调用入口有恢复 TODO。PlayerRevive 条件开关暂时关闭。
这些集成尚未完成 NeoForge API 验证，并非声称其没有 1.21.1 版本。JEI / Configured 及四类背包、Curios 已接入，验证范围见下节。

## Compatibility Restoration — 2026-09-09

- JEI：恢复编译，使用 19.53.0.426 的 NeoForge API；更新 ResourceLocation、旋转预览矩阵和 DeltaTracker，材料按实际需求数量展示。保留工作台配方类别、成品和工作台催化剂注册。
- Configured：开发运行环境加入 2.6.3（CurseForge 7276577）；配件界面的配置入口使用 NeoForge IConfigScreenFactory。运行日志确认 Configured 为 cgm 注册配置页面工厂，识别 CLIENT / COMMON / SERVER 共 3 个配置。
- 两者均为可选联动：JEI API 仅 compileOnly，JEI 和 Configured 模组使用 localRuntime，不打入 CGM jar、不作为发布运行必需依赖。
- build：PASS，D:/PRTSNote/tmp/cgm-compat-build-01.log。
- 联合客户端启动并进入现有世界：PASS，D:/PRTSNote/tmp/cgm-compat-client-01.log；JEI 完成配方注册和界面初始化，Configured 配置工厂注册成功。
- 配方页的具体布局、旋转预览，以及从扳手按钮进入配置页：尚未完成界面验收。测试期间检测到用户正在操作窗口，留待手动检查。
- 背包与 Curios 适配见下节；其后待评估 Controllable、Simple Planes、CMDCam、PlayerRevive。Catalogue、Recruits、CreativeCore 等此前开发依赖尚未恢复。
- 官方来源：JEI 1.21/1.21.1 接入说明 https://github.com/mezz/JustEnoughItems/wiki/Getting-Started-%5BMinecraft-1.21-and-1.21.1%5D ，Configured 构件 https://www.curseforge.com/minecraft/mc-mods/configured/files/7276577 。

## 优先背包兼容 — 2026-09-09

按照用户截图中的划线优先处理以下四组，Curios 作为装备位查询配套恢复。

| 联动 | 编译使用的 NeoForge 1.21.1 版本 | 适配内容 |
| --- | --- | --- |
| Backpacked | 3.0.5（CurseForge 7866688） | 使用 Augments.get 读取新版组件；保留 Quiverlink 配置门槛、多背包查弹和库存保存回调。 |
| L2 Backpack / L2 Library | 3.1.5 / 3.0.8 | 普通背包组件写回、末影背包取弹；旧 WorldChestItem 入口迁为 DimensionalItem，使用 StorageContainer.get 取得容器。 |
| Sophisticated Backpacks / Core | 3.26.2.2148 / 1.5.1.2341 | 使用现有背包遍历 API；首个背包无弹时继续搜索；复制弹药栈后通过 setStackInSlot 写回，触发库存索引与保存更新。 |
| Traveler's Backpack | 10.1.39 | CapabilityUtils 迁为 AttachmentUtils，使用 getStorage；复制后通过库存 setStackInSlot 扣弹，触发组件更新及 saveHandler。 |
| Curios | 9.5.1+1.21.1 | 使用 Optional 库存 API 和 Java Consumer，恢复 L2 背包装备位查询。 |

- 查弹优先级保持为玩家物品栏 → Backpacked → Sophisticated → Traveler's → L2；创造模式逻辑保持不变。
- 以上第三方依赖全部仅 compileOnly，不自动装入开发运行环境、不打入发布 jar，也不变为 CGM 必装依赖；仅检测到对应模组时调用适配器。
- metadata 为可选模组声明本次编译基线版本下限；这不是对所有更新版本兼容性的保证。搭配测试应优先使用上表的确切版本及其自身前置依赖。
- API 依据：实际下载的上述官方发布 jar，核查了库存遍历终止条件、组件读写与保存回调；未用第三方移植替换实现。Modrinth 版本 ID 固定在 gradle.properties。
- 官方构件来源： https://www.curseforge.com/minecraft/mc-mods/backpacked/files/7866688 、 https://modrinth.com/mod/curios 、 https://modrinth.com/mod/sophisticated-backpacks 、 https://modrinth.com/mod/sophisticated-core 、 https://modrinth.com/mod/travelersbackpack 、 https://modrinth.com/mod/l2backpack 、 https://modrinth.com/mod/l2library 。
- 初次整包构建通过：D:/PRTSNote/tmp/cgm-backpack-03.log。最终回调及 metadata 修订后 build 通过（23 秒）：D:/PRTSNote/tmp/cgm-backpack-04.log；test 为 NO-SOURCE，不代表玩法验证通过。检查发布 jar 已包含五个适配器和 NeoForge metadata，未嵌入第三方模组。
- 本批兼容文件 git diff --check 通过；全仓库检查另报既有 BulletTrailRenderingHandler.java:79 行尾空格，本批未改动该文件。
- 按用户要求，本批不启动客户端/服务器，不进行游戏测试。尚待测试人员验证：各背包单独安装、组合安装、未安装；Quiverlink 开关；首个背包无弹但后续背包有弹；扣到零与连续换弹；Curios/胸甲槽；L2 末影及维度容器；重登后的弹药持久化和多人同步。

## Remaining Issues / Validation

- Gradle 配置：PASS，2026-09-08。网络使用本机已有代理，仅命令进程设置；不在仓库写入机器专用路径或代理。
- compileJava / build：PASS，首个完整构建见 tmp/cgm-build-01.log（2026-09-09）。无现有测试 source set，不把 test NO-SOURCE 当作玩法验证。
- clean：待最终构建验证。
- runData：PASS，tmp/cgm-data-02.log；配方、标签、战利品、枪械、九种附魔生成成功。
- runClient / 世界内玩法：客户端启动、进入世界和打开物品栏已验证；完整射击/换弹/命中/配件回归仍待完成。
- runServer：PASS（启动），tmp/cgm-server-02.log；Framework/CGM 加载成功，生成世界并输出 Done，尚未验证玩家联机。

## API Migration Notes

- Framework 使用 NeoForge 1.21.1 构件（Curse file 7530361），新的 FrameworkNetworkBuilder 使用具名 StreamCodec；内部经 CustomPacketPayload / PayloadRegistrar 注册，保留全部 15 种核心消息。
- 废弃的 Framework 登录数据接口迁为 OnDatapackSyncEvent；加入服务器和重载数据包时同步枪械定义。
- 新增持久化、网络同步的弹药/颜色/标志/自定义枪械组件；配件和模型使用 ItemContainerContents，避免空注册表解码 ItemStack。
- NBT 快照修改必须显式写回；已补开火、换弹、卸弹、染色和创造栏写入。
- 附魔迁为 ResourceKey 和数据生成定义，原九种附魔的效果保留在玩法处理器；不通过给枪增加耐久度绕过附魔限制。
- TickEvent.Phase 迁为具体 Pre / Post 事件，HUD 迁为 RenderGuiLayerEvent，物品渲染扩展和菜单使用客户端注册事件。
- 以上仍处于编译修复阶段，不能视为已通过运行验证。

## Sources / Attribution

- NeoForge 1.21.1 官方文档：https://docs.neoforged.net/docs/1.21.1/gettingstarted/
- 官方 MDK：https://github.com/NeoForgeMDKs/MDK-1.21.1-ModDevGradle
- 参考移植（非代码基线）：https://github.com/createmeow/MrCrayfishGunMod/tree/1.21.1 ，初次查看 commit `00e6ec6d`。若借鉴具体实现，将逐模块记录。
- 原仓库 LICENSE-new.txt 实际为 Forge LGPL 2.1，mods.toml 声明 GPL3。两者均保留；不因参考仓库缺少许可证而删除现有声明，也不把 Forge 许可证改称枪械代码许可证。
