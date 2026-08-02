import os, urllib.request, json
TOKEN = os.environ.get('TOKEN')
if not TOKEN:
    raise SystemExit('TOKEN missing')
headers = {'Accept': 'application/vnd.github+json', 'Authorization': f'token {TOKEN}'}
run_id = '30757901511'
url = f'https://api.github.com/repos/arnolds2014-cmd/dragon-sound/actions/runs/{run_id}/jobs'
req = urllib.request.Request(url, headers=headers)
with urllib.request.urlopen(req) as resp:
    data = json.load(resp)
for job in data.get('jobs', []):
    print('JOB', job['id'], job['name'], job['status'], job['conclusion'])
    for step in job.get('steps', []):
        if step.get('conclusion') \!= 'success':
            print('  STEP', step.get('number'), repr(step.get('name')), step.get('status'), step.get('conclusion'))
