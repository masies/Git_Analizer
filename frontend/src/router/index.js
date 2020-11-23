import Vue from 'vue';
import VueRouter from 'vue-router';
import Home from '../views/Home.vue';
import Repository from '../views/Repository.vue';
import Commit from '../views/Commit.vue';
import Issue from '../views/Issue.vue';
import CommitsListContainer from '../views/CommitsListContainer.vue';
import IssuesListContainer from '../views/IssuesListContainer.vue';
import RepositoryList from '../views/RepositoryList.vue';
import ExpertiseListContainer from '../views/ExpertiseListContainer.vue';
import PullRequestStatsListContainer from '../views/PullRequestStatsListContainer.vue';
import DirectoryStructureContainer from '../views/DirectoryStructureContainer.vue';


Vue.use(VueRouter);

const routes = [
{
  path: '/',
  name: 'Home',
  component: Home,
},
{
  path: '/repository/:owner/:name',
  name: 'repository',
  component: Repository,
},
{
  path: '/repository',
  name: 'repositoryList',
  component: RepositoryList,
},
{
  path: '/repository/:owner/:name/issues',
  name: 'issuesListContainer',
  component: IssuesListContainer,
},
{
  path: '/repository/:owner/:name/commits',
  name: 'commitsListContainer',
  component: CommitsListContainer,
},
{
  path: '/repository/:owner/:name/issue/:id',
  name: 'issue',
  component: Issue,
},
{
  path: '/repository/:owner/:name/commit/:id',
  name: 'commit',
  component: Commit,
},
{
  path: '/repository/:owner/:name/tree*',
  name: 'tree',
  component: DirectoryStructureContainer,
},
{
  path: '/repository/:owner/:name/expertise',
  name: 'expertiseListContainer',
  component: ExpertiseListContainer,
},
{
  path: '/repository/:owner/:name/pullRequestStats',
  name: 'pullRequestStatsListContainer',
  component: PullRequestStatsListContainer,
}
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes,
});

export default router;
