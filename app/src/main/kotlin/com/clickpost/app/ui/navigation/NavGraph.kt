package com.clickpost.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.media3.common.util.UnstableApi
import androidx.navigation.navArgument
import com.clickpost.app.engine.ExportState
import com.clickpost.app.social.data.Platform
import com.clickpost.app.social.ui.screens.*
import com.clickpost.app.social.viewmodel.AccountGroupViewModel
import com.clickpost.app.social.viewmodel.PublishViewModel
import com.clickpost.app.ui.screens.*
import com.clickpost.app.viewmodel.MainViewModel

object Routes {
    const val SPLASH         = "splash"
    const val PROFILE_SETUP  = "profile_setup"
    const val DASHBOARD      = "dashboard"
    const val BRANDING_CTRL  = "branding_control"
    const val EXPORT         = "export"
    const val PROFILE_EDIT   = "profile_edit"
    const val ACCOUNT_GROUPS = "account_groups"
    const val CREATE_EDIT_GROUP = "create_edit_group?groupId={groupId}"
    const val PLATFORM_CREDENTIAL_FORM = "platform_credential_form/{groupId}/{platform}"
    const val PUBLISH_PROGRESS = "publish_progress/{jobId}"
    const val PUBLISH_RESULT = "publish_result/{jobId}"
}

@UnstableApi
@Composable
fun ClickPostNavGraph(
    navController: NavHostController = rememberNavController(),
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val exportState by viewModel.exportState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onAnimationFinished = {
                    val destination = if (uiState.isFirstLaunch || !uiState.hasProfile)
                        Routes.PROFILE_SETUP else Routes.DASHBOARD
                    navController.navigate(destination) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.PROFILE_SETUP) {
            ProfileSetupScreen(
                viewModel = viewModel,
                onSetupComplete = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.PROFILE_SETUP) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.DASHBOARD) {
            DashboardScreen(
                viewModel = viewModel,
                onVideoSelected = { navController.navigate(Routes.BRANDING_CTRL) },
                onEditProfile = { navController.navigate(Routes.PROFILE_EDIT) }
            )
        }

        composable(Routes.BRANDING_CTRL) {
            BrandingControlScreen(
                viewModel = viewModel,
                onExportClicked = { navController.navigate(Routes.EXPORT) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.EXPORT) {
            ExportScreen(
                viewModel = viewModel,
                exportState = exportState,
                onBack = {
                    viewModel.cancelExport()
                    navController.popBackStack()
                },
                onDone = {
                    viewModel.resetExport()
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.DASHBOARD) { inclusive = true }
                    }
                },
                onPublishStarted = { jobId ->
                    navController.navigate(Routes.PUBLISH_PROGRESS.replace("{jobId}", jobId))
                }
            )
        }

        composable(Routes.PROFILE_EDIT) {
            ProfileEditScreen(
                viewModel = viewModel,
                onManageAccounts = { navController.navigate(Routes.ACCOUNT_GROUPS) },
                onSaved = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.ACCOUNT_GROUPS) {
            val socialViewModel: AccountGroupViewModel = hiltViewModel()
            AccountGroupsScreen(
                viewModel = socialViewModel,
                onBack = { navController.popBackStack() },
                onCreateGroup = { navController.navigate(Routes.CREATE_EDIT_GROUP) },
                onEditGroup = { groupId ->
                    navController.navigate(Routes.CREATE_EDIT_GROUP.replace("{groupId}", groupId))
                }
            )
        }

        composable(
            Routes.CREATE_EDIT_GROUP,
            arguments = listOf(navArgument("groupId") { nullable = true })
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId")
            val socialViewModel: AccountGroupViewModel = hiltViewModel()
            CreateEditGroupScreen(
                groupId = groupId,
                viewModel = socialViewModel,
                onBack = { navController.popBackStack() },
                onAddCredential = { id, platform ->
                    navController.navigate("platform_credential_form/$id/${platform.name}")
                }
            )
        }

        composable(
            Routes.PLATFORM_CREDENTIAL_FORM,
            arguments = listOf(
                navArgument("groupId") { type = androidx.navigation.NavType.StringType },
                navArgument("platform") { type = androidx.navigation.NavType.StringType }
            )
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId")!!
            val platformName = backStackEntry.arguments?.getString("platform")!!
            val platform = Platform.valueOf(platformName)
            val socialViewModel: AccountGroupViewModel = hiltViewModel()
            PlatformCredentialFormScreen(
                groupId = groupId,
                platform = platform,
                viewModel = socialViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            Routes.PUBLISH_PROGRESS,
            arguments = listOf(navArgument("jobId") { type = androidx.navigation.NavType.StringType })
        ) { backStackEntry ->
            val jobId = backStackEntry.arguments?.getString("jobId")!!
            val publishViewModel: PublishViewModel = hiltViewModel()
            PublishProgressScreen(
                jobId = jobId,
                viewModel = publishViewModel,
                onNavigateToResult = { id ->
                    navController.navigate(Routes.PUBLISH_RESULT.replace("{jobId}", id)) {
                        popUpTo(Routes.PUBLISH_PROGRESS) { inclusive = true }
                    }
                }
            )
        }

        composable(
            Routes.PUBLISH_RESULT,
            arguments = listOf(navArgument("jobId") { type = androidx.navigation.NavType.StringType })
        ) { backStackEntry ->
            val jobId = backStackEntry.arguments?.getString("jobId")!!
            val publishViewModel: PublishViewModel = hiltViewModel()
            PublishResultScreen(
                jobId = jobId,
                viewModel = publishViewModel,
                onBackToDashboard = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.DASHBOARD) { inclusive = true }
                    }
                }
            )
        }
    }
}
