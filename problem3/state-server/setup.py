from distutils.core import setup

setup(
    author='Peter M Rokowski',
    version='0.1.0',
    author_email='peterrokowski@gmail.com',
    description='A RESTful server that can be queried with' +
    'longitude and latitude points, returning the state that' +
    'the point lies in within the US, or \'unrecognized ' +
    'point\' otherwise',
    install_requires=[
        'flask',
        'sympy',
    ],
)
