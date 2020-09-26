import Vue from 'vue';
import VueRouter from 'vue-router';
import Home from '../views/Home.vue';
import Repository from '../views/Repository.vue';
import Commit from '../views/Commit.vue';
import Issue from '../views/Issue.vue';
import CommitsList from '../views/CommitsList.vue';
import IssuesList from '../views/IssuesList.vue';
import RepositoryList from '../views/RepositoryList.vue';


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
  name: 'issuesList',
  component: IssuesList,
},
{
  path: '/repository/:owner/:name/commits',
  name: 'commitsList',
  component: CommitsList,
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
}
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes,
});

export default router;
