import json
import argparse
from sympy import Point, Polygon

STATE_FILE = 'states.json'


def debug_ingest(my_dict):
    import pprint
    pp = pprint.PrettyPrinter(indent=4)
    pp.pprint(my_dict)


def ingest_data(file_path):
    state_dict = {}
    # state_dict["states"] = []

    with open(file_path) as state_file:
        line = ""
        begin = False
        end = False

        # due to lack of Line Endings it only reads one char at a time
        # need to manually reconstruct lines
        for char in state_file.read():
            # start tracking the dictionary
            if char is '{':
                begin = True

            # end tracking
            if char is '}':
                end = True

            # if we're tracking add to the line
            if begin:
                line = line + char

            # reset everything to process the next state object
            if end:
                # print("line: %s" % line)
                raw = json.loads(line)
                # add the state to our dict
                state_dict[raw['state']] = raw['border']

                # reset the begin and end values
                end = False
                begin = False

                # clear the line
                line = ""

    debug_ingest(state_dict)
    return state_dict


def within_state(latidute, longitude, border_array):
    print("debug:\n    lat:: %s\n    lon:: %s\n    borders:: %s" %
          (latidute, longitude, border_array))

    test_point = Point(latidute, longitude)
    print("Point used successfully")

    Point_arr = [Point(x[0], x[1]) for x in border_array]

    Pennsylvania_Polygon = Polygon(*Point_arr)

    print("Pennsylvania area: %s" % Pennsylvania_Polygon)

    return Pennsylvania_Polygon.encloses_point(test_point)


if __name__ == '__main__':
    print("script initialized")

    parser = argparse.ArgumentParser(
        description='Determine the state that given coords are in')
    parser.add_argument('--latitude', '-lat', type=float, required=True,
                        help='latitude coordinate')
    parser.add_argument('--longitude', '-lon', type=float, required=True,
                        help='longitude coordinate')

    args = parser.parse_args()

    if args.latitude is None or args.longitude is None:
        # exit
        print("bad data, exiting")
    else:
        # throw in flag to handle re-ingestion or using serialized db for runs
        # convert to a large list, we're not storing anything else
        states = ingest_data(STATE_FILE)
        # need to re-do the dictionary

        in_pa = within_state(args.latitude, args.longitude,
                     states['Pennsylvania'])

        print("is it in PA? \n %s" % in_pa)
        # we may proceed
